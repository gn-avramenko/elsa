import React, { useState } from 'react';
import { useRouter } from './router';
import { BaseReactUiElement } from '@/common/component';
import { EditorContext } from '@/common/editor';

export function EditorWrapper(
    props: React.PropsWithChildren<{ element: BaseReactUiElement }>
) {
    const [hasChanges, setHasChanges] = useState<boolean>(false);
    const processHasChanges = (value: boolean) => {
        setHasChanges(value);
        if (router) {
            router.setHasChanges(value);
        }
        props.element.findByTag('saveButton').stateSetters.get('disabled')!(!value);
    };
    (props.element as any).processResetChanges = () => {
        processHasChanges(false);
    };
    const router = useRouter();
    return (
        <EditorContext.Provider
            value={{
                hasChanges,
                setHasChanges: processHasChanges,
            }}
        >
            <div
                className="webpeer-container"
                style={{ display: 'flex', flexDirection: 'row' }}
            >
                {props.element.findByTag('title').createReactElement()}
                {props.element.findByTag('backButton').createReactElement()}
                {props.element.findByTag('saveButton').createReactElement()}
            </div>
            <div>
                {props.element.children
                    ?.filter(
                        (it) =>
                            it.tag !== 'title' &&
                            it.tag !== 'backButton' &&
                            it.tag !== 'saveButton'
                    )
                    .map((it) => (it as BaseReactUiElement).createReactElement())}
            </div>
        </EditorContext.Provider>
    );
}
