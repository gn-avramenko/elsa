import { WebComponentWrapper } from '@/common/wrapper';
import { WebAppSkeleton } from '@g/app/WebAppSkeleton';
import { BaseReactUiElement } from '@/common/component';

function WebAppFC(props: { element: WebAppComponent }) {
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
        </WebComponentWrapper>
    );
}

export class WebAppComponent extends WebAppSkeleton {
    functionalComponent = WebAppFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
