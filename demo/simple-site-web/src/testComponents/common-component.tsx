import { ReactElement } from 'react';
import {
    BaseUiElement,
    PreloaderMiddleware,
    webpeerExt,
    WebPeerExtension,
} from 'webpeer-core';
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

    counter = 1;

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
        this.state.set('counter', this.counter);
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

export const registerFactory = (type: string, factory: ReactUiElementFactory) => {
    webpeerExt.elementTypes = webpeerExt.elementTypes || [];
    webpeerExt.elementTypes.push(type);
    reactWebPeerExt.elementHandlersFactories.set(type, factory);
};

let root: Root | null = null;

export const preloaderHolder = {
    showPreloader: () => {},
    hidePreloader: () => {},
};

reactWebPeerExt.setMiddleware([
    new PreloaderMiddleware(
        {
            showPreloader() {
                preloaderHolder.showPreloader();
            },
            hidePreloader() {
                preloaderHolder.hidePreloader();
            },
        },
        {
            delay: 300,
        }
    ),
]);
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

export function debounce<T extends (...args: any[]) => any>(
    func: T,
    wait: number
): (...args: Parameters<T>) => void {
    let timeoutId: ReturnType<typeof setTimeout> | null = null;

    return (...args: Parameters<T>) => {
        if (timeoutId) {
            clearTimeout(timeoutId);
        }

        timeoutId = setTimeout(() => {
            func.apply(null, args);
        }, wait);
    };
}

export type TestSortDirection = 'ASC' | 'DESC';
export type TestSort = {
    fieldId: string;
    direction: TestSortDirection;
};
export type TestEntityListColumnType = 'TEXT' | 'MENU' | 'CUSTOM' | 'OPTION';

export type TestColumnDescription = {
    title: string;
    id: string;
    type: TestEntityListColumnType;
    customSubtype?: string;
    sortable: boolean;
};

export type TestOption = {
    id: string;
    displayName?: string;
};

export type TableAction = {
    columnId: string;
    rowId: string;
    actionId: string;
};
