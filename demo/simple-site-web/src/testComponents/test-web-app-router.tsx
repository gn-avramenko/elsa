import { BaseReactUiElement, ReactUiElementFactory } from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';
import { RouterContext } from './common-router';

function WebAppRouterComponent(props: { element: WebAppRouter }) {
    for (const prop of props.element.state.keys()) {
        const [value, setValue] = useState(props.element.state.get(prop));
        props.element.state.set(prop, value);
        props.element.stateSetters.set(prop, setValue);
    }
    function handlePopEvent() {
        const currentPath = props.element.state.get('path') as string;
        const hasChanges = props.element.state.get('hasChanges') as boolean;
        const message = props.element.state.get('confirmMessage') as string;
        if (hasChanges) {
            if (!window.confirm(message)) {
                window.history.pushState(null, '', currentPath);
                return;
            }
        }
        props.element.navigate({ path: window.location.pathname, force: true });
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
        <RouterContext.Provider
            value={{
                hasChanges: props.element.isHasChanges(),
                setHasChanges: (value) => props.element.setHasChanges(value),
                confirmMessage: props.element.getConfirmMessage(),
                setConfirmMessage: (message) =>
                    props.element.setConfirmMessage(message),
            }}
        >
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
        </RouterContext.Provider>
    );
}

export class WebAppRouter extends BaseReactUiElement {
    constructor(model: any) {
        super([], ['path', 'hasChanges', 'confirmMessage'], [], ['navigate'], model);
    }
    navigate(value: { path: string; force: boolean }) {
        this.sendCommand('navigate', value, false);
    }

    isHasChanges() {
        return this.state.get('hasChanges') as boolean;
    }
    setHasChanges(value: boolean) {
        this.state.set('hasChanges', value);
        this.stateSetters.get('hasChanges')!(value);
        this.sendPropertyChange('hasChanges', value, true);
    }

    getConfirmMessage() {
        return this.state.get('confirmMessage') as string | undefined;
    }
    setConfirmMessage(value?: string) {
        this.state.set('confirmMessage', value);
        this.stateSetters.get('confirmMessage')!(value);
    }
    protected updatePropertyValue(pn: string, pv: any) {
        super.updatePropertyValue(pn, pv);
        if (pn === 'path') {
            window.history.pushState(null, '', pv);
            return;
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
