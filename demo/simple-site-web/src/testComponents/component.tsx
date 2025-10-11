import React, { ReactElement } from 'react';
import { BaseUiElement } from 'webpeer-core';
import { reactWebPeerExt } from './web-peer';

export type ReactElementDescription = {
    state: string[];
    actionsFromServer: string[];
    actionsFromClient: string[];
};
export abstract class BaseReactUiElement extends BaseUiElement {
    readonly description: ReactElementDescription;

    state = new Map<string, any | null | undefined>();

    counter = 1;

    stateSetters: Map<string, (value?: any) => void> = new Map<
        string,
        (value?: any) => void
    >();

    createReactElement(): React.ReactElement {
        return React.createElement(this.functionalComponent, {
            element: this,
            key: this.id,
        });
    }

    abstract functionalComponent: (props: any) => ReactElement;

    protected constructor(description: ReactElementDescription, model: any) {
        super(model);
        this.description = description;
        (model.children || []).forEach((ch: any) => {
            const elm = reactWebPeerExt.elementHandlersFactories
                .get(ch.type)!
                .createElement(ch);
            elm.parent = this;
            elm.init();
            this.children = this.children || [];
            this.children.push(elm);
        });
        this.state.set('counter', this.counter);
    }

    protected findByTag(tag: string) {
        return this.children?.find((it) => it.tag === tag) as BaseReactUiElement;
    }

    getState(): any {
        const result = super.getState();
        this.state.forEach((value, key) => {
            result[key] = value;
        });
        return result;
    }

    redraw() {
        this.counter++;
        this.stateSetters.get('counter')!(this.counter);
    }

    processCommandFromServer(commandId: string, data?: any) {
        if (commandId === 'pc') {
            this.updatePropertyValue(data!.pn, data!.pv);
            return;
        }
        super.processCommandFromServer(commandId, data);
    }

    protected updatePropertyValue(pn: string, pv: any) {
        this.state.set(pn, pv);
        this.stateSetters.get(pn)?.call(this, pv);
    }
}
