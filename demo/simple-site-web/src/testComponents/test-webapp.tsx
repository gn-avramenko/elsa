import {
    BaseReactUiElement,
    FlexDirection,
    preloaderHolder,
    ReactUiElementFactory,
} from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';

import './scaffold-styles.css';
import { api } from 'webpeer-core';
import ToastNotification from './notification';
import Preloader from './preloader';

function WebAppComponent(props: { element: TestWebapp }) {
    const [isLoading, setIsLoading] = useState<boolean>(false);
    preloaderHolder.hidePreloader = () => setIsLoading(false);
    preloaderHolder.showPreloader = () => setIsLoading(true);
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
    const [isToastOpen, setIsToastOpen] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const [toastType, setToastType] = useState<
        'success' | 'error' | 'info' | 'warning'
    >('info');

    const showToast = (
        message: string,
        type: 'success' | 'error' | 'info' | 'warning' = 'info'
    ) => {
        setToastMessage(message);
        setToastType(type);
        setIsToastOpen(true);
    };

    props.element.notify = (message) => showToast(message, 'info');
    const closeToast = () => {
        setIsToastOpen(false);
    };

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
                    flexDirection:
                        props.element.flexDirection === 'ROW' ? 'row' : 'column',
                }}
            >
                {props.element.findByTag('navigation').createReactElement()}
                {props.element.findByTag('router').createReactElement()}
            </div>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
            <ToastNotification
                isOpen={isToastOpen}
                onClose={closeToast}
                message={toastMessage}
                type={toastType}
                duration={2000}
            />
            {isLoading ? <Preloader text="Loading" /> : ''}
        </div>
    );
}

export class TestWebapp extends BaseReactUiElement {
    readonly flexDirection: FlexDirection;
    notify: (value: string) => void = () => {};
    constructor(model: any) {
        super([], [], [], [], model);
        this.flexDirection = model.flexDirection;
    }
    createReactElement(): React.ReactElement {
        return React.createElement(WebAppComponent, { element: this, key: this.id });
    }

    processCommandFromServer(commandId: string, data?: any) {
        if (commandId === 'notify') {
            this.notify(data.message);
            return;
        }
        if (commandId === 'confirm') {
            const message = data!.message;
            const cmd = data!.cmd;
            const elementId = data!.id;
            const cmdData = data!.data;
            if (window.confirm(message)) {
                api.sendCommandAsync(elementId, cmd, cmdData, false);
            }
            return;
        }
        super.processCommandFromServer(commandId, data);
    }
}

export class WebAppFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestWebapp(model);
    }
}
