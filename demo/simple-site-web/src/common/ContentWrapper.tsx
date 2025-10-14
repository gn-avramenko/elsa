import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { ContentWrapperSkeleton } from '@g/common/ContentWrapperSkeleton';

function ContentWrapperFC(props: { element: ContentWrapperComponent }) {
    initStateSetters(props.element);
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

export class ContentWrapperComponent extends ContentWrapperSkeleton {
    functionalComponent = ContentWrapperFC;
    getProcessedChildren(): string[] {
        return [];
    }
    
}
