import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { SecuritySectionSkeleton } from '@g/security/SecuritySectionSkeleton';

function SecuritySectionFC(props: { element: SecuritySectionComponent }) {
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
                Security
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

export class SecuritySectionComponent extends SecuritySectionSkeleton {
    functionalComponent = SecuritySectionFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
