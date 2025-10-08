import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { OrganizationEditorSkeleton } from '@g/organization/OrganizationEditorSkeleton';

function OrganizationEditorFC(props: { element: OrganizationEditorComponent }) {
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
                Organization
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

export class OrganizationEditorComponent extends OrganizationEditorSkeleton {
    functionalComponent = OrganizationEditorFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
