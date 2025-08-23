import { BaseReactUiElement, ReactUiElementFactory } from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';

function LanguageSelectorComponent(props: { element: TestLanguageSelector }) {
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
            <select
                key="select"
                className="webpeer-select"
                value={props.element.getSelectedId()}
                onChange={(e) => props.element.setSelectedId(e.target.value)}
            >
                {props.element.getOptions().map((it) => (
                    <option key={it.id} value={it.id}>
                        {it.displayName}
                    </option>
                ))}
            </select>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class TestLanguageSelector extends BaseReactUiElement {
    constructor(model: any) {
        super([], ['options', 'selectedId'], [], ['select'], model);
    }

    getOptions() {
        return this.state.get('options') as {
            id: string;
            displayName: string;
        }[];
    }
    getSelectedId() {
        return this.state.get('selectedId') as string;
    }

    setSelectedId(id: string) {
        this.stateSetters.get('selectedId')!(id);
        this.sendCommand('select', {
            selectedId: id,
        });
    }

    createReactElement(): React.ReactElement {
        return React.createElement(LanguageSelectorComponent, {
            element: this,
            key: this.id,
        });
    }
}

export class TestLanguageSelectorFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestLanguageSelector(model);
    }
}
