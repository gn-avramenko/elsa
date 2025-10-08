import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { OrganizationsSectionSkeleton } from '@g/organization/OrganizationsSectionSkeleton';

function OrganizationsSectionFC(props: { element: OrganizationsSectionComponent }) {
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
                Organizations
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

export class OrganizationsSectionComponent extends OrganizationsSectionSkeleton {
    functionalComponent = OrganizationsSectionFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
