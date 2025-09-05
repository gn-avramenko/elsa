import {
    BaseReactUiElement,
    FlexDirection,
    ReactUiElementFactory,
} from './common-component';
import React from 'react';

export class WrapperWebapp extends BaseReactUiElement {
    readonly flexDirection: FlexDirection;
    constructor(model: any) {
        super([], [], [], [], model);
        this.flexDirection = model.flexDirection;
    }
    createReactElement(): React.ReactElement {
        throw new Error('illegal call');
    }
}

export class TestWrapperFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new WrapperWebapp(model);
    }
}
