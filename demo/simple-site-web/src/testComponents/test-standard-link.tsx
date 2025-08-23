import { BaseReactUiElement, ReactUiElementFactory } from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';

function StandardLinkComponent(props: { element: TestStandardLink }) {
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
            <button
                className="webpeer-button"
                onClick={() => props.element.processOnClick()}
            >
                {props.element.getTitle()}
            </button>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class TestStandardLink extends BaseReactUiElement {
    constructor(model: any) {
        super([], ['title'], [], ['click'], model);
    }

    processOnClick() {
        this.sendCommand('click');
    }

    getTitle() {
        return this.state.get('title') as string;
    }

    createReactElement(): React.ReactElement {
        return React.createElement(StandardLinkComponent, {
            element: this,
            key: this.id,
        });
    }
}

export class TestStandardLinkFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestStandardLink(model);
    }
}
