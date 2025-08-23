import { BaseReactUiElement, ReactUiElementFactory } from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';

function WebAppRouterComponent(props: { element: WebAppRouter }) {
    for (const prop of props.element.state.keys()) {
        const [value, setValue] = useState(props.element.state.get(prop));
        props.element.state.set(prop, value);
        props.element.stateSetters.set(prop, setValue);
    }
    function handlePopEvent() {
        props.element.navigate({ path: window.location.pathname });
    }
    useEffect(() => {
        props.element.state.forEach((value, key) => {
            props.element.stateSetters.get(key)?.(value);
        });
        window.addEventListener('popstate', handlePopEvent);
        return () => {
            window.removeEventListener('popstate', handlePopEvent);
        };
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
                <div style={{ flexGrow: 0 }} key="id">
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
            <div
                className="webpeer-container-content"
                key="content"
                style={{
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                {props.element.findByTag('content').createReactElement()}
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

export class WebAppRouter extends BaseReactUiElement {
    constructor(model: any) {
        super([], ['path'], [], ['navigate'], model);
    }
    navigate(value: { path: string }) {
        this.sendCommand('navigate', value, false);
    }

    protected updatePropertyValue(pn: string, pv: any) {
        super.updatePropertyValue(pn, pv);
        if (pn === 'path') {
            window.history.pushState(null, '', pv);
        }
    }

    createReactElement(): React.ReactElement {
        return React.createElement(WebAppRouterComponent, {
            element: this,
            key: this.id,
        });
    }
}

export class TestWebAppRouterFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new WebAppRouter(model);
    }
}
