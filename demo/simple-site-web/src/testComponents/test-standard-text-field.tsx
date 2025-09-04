import {
    BaseReactUiElement,
    debounce,
    ReactUiElementFactory,
} from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';
import { useEditor } from './common-editor';

function StandardTextFieldComponent(props: { element: TestStandardTextField }) {
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
    const editor = useEditor();

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
            <input
                type="text"
                value={props.element.getValue()}
                className={`webpeer-text-field${props.element.getValidationMessage() ? ' has-error' : ''}`}
                onChange={(e) => {
                    props.element.setValue(e.target.value);
                    props.element.setValidationMessage(undefined);
                    if (editor) {
                        editor.setHasChanges(true);
                    }
                }}
            />
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class TestStandardTextField extends BaseReactUiElement {
    constructor(model: any) {
        super(
            ['deferred', 'debounceTime'],
            ['value', 'hidden', 'disabled', 'trackValueChange', 'validationMessage'],
            [],
            ['value-changed'],
            model
        );
    }

    private handleValueChanged = debounce(() => {
        this.sendCommand('value-changed');
    }, this.getDebounceTime());
    isDeferred() {
        return this.initParams.get('deferred') as boolean;
    }

    getDebounceTime() {
        return this.initParams.get('debounceTime') as number;
    }
    getValue() {
        return this.state.get('value') as string;
    }
    isHidden() {
        return this.state.get('hidden') as boolean;
    }
    isTrackValueChange() {
        return this.state.get('trackValueChange') as boolean;
    }
    isDisabled() {
        return this.state.get('disabled') as boolean;
    }

    getValidationMessage() {
        return this.state.get('validationMessage') as boolean;
    }
    setValidationMessage(value?: string) {
        this.state.set('validationMessage', value);
        this.stateSetters.get('validationMessage')!(value);
        this.sendPropertyChange('valdationMessage', value, true);
    }

    setValue(value?: string) {
        this.stateSetters.get('value')!(value);
        this.sendCommand(
            'pc',
            {
                pn: 'value',
                pv: value,
            },
            this.isDeferred()
        );
        if (this.isTrackValueChange()) {
            this.handleValueChanged();
        }
    }

    createReactElement(): React.ReactElement {
        return React.createElement(StandardTextFieldComponent, {
            element: this,
            key: this.id,
        });
    }
}

export class TestStandardTextFieldFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestStandardTextField(model);
    }
}
