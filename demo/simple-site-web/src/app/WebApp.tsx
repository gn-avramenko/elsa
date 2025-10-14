import { WebComponentWrapper } from '@/common/wrapper';
import { WebAppSkeleton } from '@g/app/WebAppSkeleton';
import { BaseReactUiElement, preloaderHolder } from '@/common/component';
import { useState } from 'react';
import { Loader } from '@/common/preloader';

function WebAppFC(props: { element: WebAppComponent }) {
    const [isLoading, setIsLoading] = useState<boolean>(false);
    preloaderHolder.hidePreloader = () => setIsLoading(false);
    preloaderHolder.showPreloader = () => setIsLoading(true);
    return (
        <WebComponentWrapper element={props.element}>
            <div
                className="webpeer-container-content"
                key="content"
                style={{
                    display: 'flex',
                    flexDirection:
                        props.element.getFlexDirection() === 'ROW' ? 'row' : 'column',
                }}
            >
                {(props.element.children || []).map((it) => {
                    if (props.element.getProcessedChildren().indexOf(it.id) === -1) {
                        return (it as BaseReactUiElement).createReactElement();
                    }
                    return null;
                })}
            </div>
            {isLoading ? <Loader /> : ''}
        </WebComponentWrapper>
    );
}

export class WebAppComponent extends WebAppSkeleton {
    functionalComponent = WebAppFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
