import { ReactElement } from 'react';
import { BaseUiElement, webpeerExt, WebPeerExtension } from 'webpeer-core';
import { createRoot, Root } from 'react-dom/client';

export interface ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement;
}

export type ReactWebPeerExtension = WebPeerExtension & {
    elementHandlersFactories: Map<string, ReactUiElementFactory>;
};
export const reactWebPeerExt = webpeerExt as ReactWebPeerExtension;

reactWebPeerExt.elementHandlersFactories = new Map();

export type FlexDirection = 'ROW' | 'COLUMN';

export abstract class BaseReactUiElement extends BaseUiElement {
    initParams = new Map<string, any | null | undefined>();

    state = new Map<string, any | null | undefined>();

    actionsFromServer: string[] = [];

    actionsFromClient: string[] = [];

    stateSetters: Map<string, (value?: any) => void> = new Map<
        string,
        (value?: any) => void
    >();

    abstract createReactElement(): ReactElement;

    protected constructor(
        initParams: string[],
        stateParams: string[],
        actionsFromServer: string[],
        actionsFromClient: string[],
        model: any
    ) {
        super(model);
        (model.children || []).forEach((ch: any) => {
            const elm = reactWebPeerExt.elementHandlersFactories
                .get(ch.type)!
                .createElement(ch);
            elm.parent = this;
            elm.init();
            this.children = this.children || [];
            this.children.push(elm);
        });
        initParams.forEach((key) => this.initParams.set(key, model[key]));
        stateParams.forEach((key) => this.state.set(key, model[key]));
        this.actionsFromClient = actionsFromClient;
        this.actionsFromServer = actionsFromServer;
    }

    public findByTag(tag: string) {
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
        //noops
    }

    processCommandFromServer(commandId: string, data?: any) {
        if (commandId === 'pc') {
            this.updatePropertyValue(data!.pn, data!.pv);
            return;
        }
        super.processCommandFromServer(commandId, data);
    }

    protected updatePropertyValue(pn: string, pv: any) {
        this.stateSetters.get(pn)!(pv);
    }
}

export const registerFactory = (type: string, factory: ReactUiElementFactory) => {
    webpeerExt.elementTypes = webpeerExt.elementTypes || [];
    webpeerExt.elementTypes.push(type);
    reactWebPeerExt.elementHandlersFactories.set(type, factory);
};

let root: Root | null = null;

reactWebPeerExt.uiHandler = {
    drawUi(rootElm: BaseReactUiElement) {
        if (!root) {
            root = createRoot(document.getElementById('root') as Element);
        }
        root.render(rootElm.createReactElement());
    },
    createElement(model: any): BaseUiElement {
        return reactWebPeerExt.elementHandlersFactories
            .get(model.type)!
            .createElement(model);
    },
    handleServerUpdate() {
        if (confirm('Server was updated, reload?')) {
            window.location.reload();
        }
    },
};
