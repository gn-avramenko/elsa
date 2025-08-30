import {
    BaseReactUiElement,
    ReactUiElementFactory,
    TestOption,
} from './common-component';
import React, {
    ChangeEvent,
    KeyboardEvent,
    useCallback,
    useEffect,
    useRef,
    useState,
} from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';

function AutocompleteFieldComponent(props: { element: TestAutocompleteField }) {
    for (const prop of props.element.state.keys()) {
        const [value, setValue] = useState(props.element.state.get(prop));
        props.element.state.set(prop, value);
        props.element.stateSetters.set(prop, setValue);
    }
    useEffect(() => {
        props.element.state.forEach((value, key) => {
            props.element.stateSetters.get(key)?.(value);
        });
    }, [props.element]);
    const [inputValue, setInputValue] = useState<string>('');
    const [suggestions, setSuggestions] = useState<TestOption[]>([]);
    props.element.suggestionsSetter = setSuggestions;
    const [selectedItems, setSelectedItems] = useState<TestOption[]>(
        props.element.getValues()
    );
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    props.element.loadingSetter = setIsLoading;

    const inputRef = useRef<HTMLInputElement>(null);
    const timeoutRef = useRef<number | null>(null);
    const abortControllerRef = useRef<AbortController | null>(null);

    // Функция для проверки, выбран ли уже элемент
    const isItemSelected = useCallback(
        (item: TestOption): boolean => {
            return selectedItems.some((selectedItem) => selectedItem.id === item.id);
        },
        [selectedItems]
    );

    // Функция для запроса данных с сервера
    const fetchSuggestions = useCallback(
        (query?: string) => {
            // Отменяем предыдущий запрос
            if (abortControllerRef.current) {
                abortControllerRef.current.abort();
            }

            abortControllerRef.current = new AbortController();

            setIsLoading(true);
            setError(null);
            props.element.requestSuggestions(query);
        },
        [isItemSelected]
    );

    // Debounce функция
    const debouncedFetch = useCallback(
        (query: string): void => {
            if (timeoutRef.current !== null) {
                window.clearTimeout(timeoutRef.current);
            }

            timeoutRef.current = window.setTimeout(() => {
                fetchSuggestions(query);
            }, props.element.getDebounceTime());
        },
        [fetchSuggestions, props.element]
    );

    // Обработка ввода с debounce
    const handleInputChange = useCallback(
        (e: ChangeEvent<HTMLInputElement>): void => {
            const value = e.target.value;
            setInputValue(value);
            setIsOpen(true);
            setIsLoading(true);
            debouncedFetch(value);
        },
        [debouncedFetch]
    );

    // Обработка выбора элемента
    const handleSelect = (item: TestOption): void => {
        if (!isItemSelected(item)) {
            const newSelectedItems = props.element.isMultiple()
                ? [...selectedItems, item]
                : [item];
            setSelectedItems(newSelectedItems);
            setInputValue('');
            setIsOpen(false);
            setSuggestions([]);

            // Колбэк при изменении выбранных элементов
            props.element.setValues(newSelectedItems);

            inputRef.current?.focus();
        }
    };

    // Удаление выбранного элемента
    const handleRemove = (itemToRemove: TestOption): void => {
        const newSelectedItems = selectedItems.filter(
            (item) => item.id !== itemToRemove.id
        );
        setSelectedItems(newSelectedItems);

        // Колбэк при изменении выбранных элементов
        props.element.setValues(newSelectedItems);
    };

    // Обработка клавиш
    const handleKeyDown = useCallback(
        (e: KeyboardEvent<HTMLInputElement>): void => {
            if (e.key === 'ArrowDown' && inputValue === '') {
                setIsOpen(true);
                setIsLoading(true);
                debouncedFetch('');
            } else if (
                e.key === 'Backspace' &&
                inputValue === '' &&
                selectedItems.length > 0
            ) {
                handleRemove(selectedItems[selectedItems.length - 1]);
            } else if (e.key === 'Escape') {
                setIsOpen(false);
            } else if (e.key === 'Enter' && suggestions.length > 0 && !isLoading) {
                // Выбираем первый элемент при нажатии Enter
                e.preventDefault();
                handleSelect(suggestions[0]);
            }
        },
        [inputValue, selectedItems, suggestions, isLoading, handleRemove, handleSelect]
    );

    // Очистка при размонтировании
    useEffect(() => {
        return () => {
            if (timeoutRef.current !== null) {
                window.clearTimeout(timeoutRef.current);
            }
            if (abortControllerRef.current) {
                abortControllerRef.current.abort();
            }
        };
    }, []);
    const modal = useModal();
    return (
        <div
            className="webpeer-container"
            key={props.element.id}
            style={{
                display: 'flex',
                flexDirection: 'column',
            }}
        >
            <div
                className="webpeer-container-header"
                key="header"
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                }}
            >
                <div style={{ flexGrow: 0 }} className="webpeer-tag" key="id">
                    {props.element.tag}
                </div>
                <div style={{ flexGrow: 1 }} key="glue" />
                <button
                    className="webpeer-details-button"
                    style={{ flexGrow: 0 }}
                    onClick={() => {
                        modal.open();
                    }}
                >
                    Details
                </button>
            </div>
            <div className="autocomplete-container">
                {/* Выбранные элементы */}
                <div className="selected-items">
                    {selectedItems.map((item) => (
                        <span key={item.id} className="selected-item">
                            {item.displayName}
                            <button
                                type="button"
                                onClick={() => {
                                    handleRemove(item);
                                }}
                                className="remove-btn"
                                aria-label={`Удалить ${item.displayName}`}
                            >
                                ×
                            </button>
                        </span>
                    ))}
                </div>

                {/* Поле ввода */}
                <div className="input-wrapper">
                    <input
                        name="autocomplete"
                        ref={inputRef}
                        type="text"
                        value={inputValue}
                        onChange={handleInputChange}
                        onKeyDown={handleKeyDown}
                        onFocus={() => setIsOpen(true)}
                        className="autocomplete-input"
                        disabled={isLoading}
                        aria-expanded={isOpen}
                        aria-haspopup="listbox"
                        aria-controls="suggestions-list"
                    />
                    {isLoading && <div className="loading-indicator">⌛</div>}
                </div>

                {/* Выпадающий список */}
                {isOpen && (
                    <div className="suggestions-container">
                        {isLoading ? (
                            <div className="loading-message">Загрузка...</div>
                        ) : error ? (
                            <div className="error-message">{error}</div>
                        ) : suggestions.length > 0 ? (
                            <ul
                                id="suggestions-list"
                                className="suggestions-list"
                                role="listbox"
                                aria-label="Предложения"
                            >
                                {suggestions.map((item) => (
                                    <li
                                        key={item.id}
                                        onClick={() => {
                                            handleSelect(item);
                                        }}
                                        className="suggestion-item"
                                        role="option"
                                        aria-selected={isItemSelected(item)}
                                    >
                                        {item.displayName}
                                    </li>
                                ))}
                            </ul>
                        ) : inputValue && !isLoading ? (
                            <div className="no-suggestions">
                                Ничего не найдено для "{inputValue}"
                            </div>
                        ) : null}
                    </div>
                )}
            </div>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class TestAutocompleteField extends BaseReactUiElement {
    constructor(model: any) {
        super(
            ['deferred', 'debounceTime', 'multiple', 'limit'],
            ['values', 'hidden', 'disabled', 'trackValueChange'],
            ['set-suggestions'],
            ['value-changed', 'request-suggestions'],
            model
        );
    }

    isMultiple() {
        return this.initParams.get('multiple') as boolean;
    }

    isDeferred() {
        return this.initParams.get('deferred') as boolean;
    }

    suggestionsSetter: (suggestions: TestOption[]) => void = () => {};

    loadingSetter: (loading: boolean) => void = () => {};

    getDebounceTime() {
        return this.initParams.get('debounceTime') as number;
    }

    getLimit() {
        return this.initParams.get('limit') as number;
    }
    getValues() {
        return this.state.get('values') as TestOption[];
    }
    isHidden() {
        return this.state.get('hiddens') as boolean;
    }
    isTrackValueChange() {
        return this.state.get('trackValueChange') as boolean;
    }
    isDisabled() {
        return this.state.get('disabled') as boolean;
    }

    requestSuggestions(query?: string) {
        this.sendCommand('request-suggestions', {
            query,
            limit: this.getLimit(),
        });
    }

    setValues(values: TestOption[]) {
        this.stateSetters.get('values')!(values);
        this.sendCommand(
            'pc',
            {
                pn: 'values',
                pv: values,
            },
            this.isDeferred()
        );
        if (this.isTrackValueChange()) {
            this.sendCommand('value-changed');
        }
    }

    processCommandFromServer(commandId: string, data?: any) {
        if (commandId === 'set-suggestions') {
            this.suggestionsSetter(data);
            this.loadingSetter(false);
            return;
        }
        super.processCommandFromServer(commandId, data);
    }

    createReactElement(): React.ReactElement {
        return React.createElement(AutocompleteFieldComponent, {
            element: this,
            key: this.id,
        });
    }
}

export class TestAutocompleteFieldFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestAutocompleteField(model);
    }
}
