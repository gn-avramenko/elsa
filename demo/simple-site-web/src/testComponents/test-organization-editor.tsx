import {
    BaseReactUiElement,
    FlexDirection,
    ReactUiElementFactory,
} from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';
import { Editor, EditorWrapper } from './common-editor';
import { RouterContextType, useRouter } from './common-router';

function OrganizationEditorComponent(props: { element: TestOrganizationEditor }) {
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
    props.element.router = useRouter();
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
            <EditorWrapper element={props.element}>
                {props.element.findByTag('name').createReactElement()}
                {props.element.findByTag('address').createReactElement()}
                {props.element.findByTag('contacts').createReactElement()}
                {props.element.findByTag('country').createReactElement()}
            </EditorWrapper>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class TestOrganizationEditor extends BaseReactUiElement implements Editor {
    constructor(model: any) {
        super(['flexDirection'], ['hasChanges', 'title', 'dataLoading'], [], [], model);
    }

    isDataLoading = () => {
        return this.state.get('dataLoading') as boolean;
    };

    router?: RouterContextType;
    getTitle(): string {
        return this.state.get('title') as string;
    }
    onBackButtonPressed = () => {
        this.sendCommand('back');
    };
    onSaveButtonPressed = () => {
        this.sendCommand('save');
    };

    protected updatePropertyValue(pn: string, pv: any) {
        super.updatePropertyValue(pn, pv);
        if (pn === 'hasChanges') {
            if (!pv && this.router) {
                this.router.setHasChanges(false);
            }
            return;
        }
    }

    getFlexDirection() {
        return this.initParams.get('flexDirection') as FlexDirection;
    }
    createReactElement(): React.ReactElement {
        return React.createElement(OrganizationEditorComponent, {
            element: this,
            key: this.id,
        });
    }

    isHasChanges() {
        return this.state.get('hasChanges') as boolean;
    }

    setHasChanges(value: boolean) {
        this.state.set('hasChanges', value);
        this.stateSetters.get('hasChanges')!(value);
    }

    processCommandFromServer(commandId: string, data?: any) {
        if (commandId === 'load-data') {
            this.sendCommand('load-data');
            return;
        }
        super.processCommandFromServer(commandId, data);
    }
}

export class TestOrganizationEditorFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestOrganizationEditor(model);
    }
}
