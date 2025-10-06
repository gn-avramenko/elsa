import { WebComponentWrapper } from '@/common/wrapper';
import { NavigationPanelSkeleton } from '@g/app/NavigationPanelSkeleton';
import { BaseReactUiElement } from '@/common/component';

function NavigationPanelFC(props: { element: NavigationPanelComponent }) {
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

export class NavigationPanelComponent extends NavigationPanelSkeleton {
    functionalComponent = NavigationPanelFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
