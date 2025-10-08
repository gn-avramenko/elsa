import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { ClientsSectionSkeleton } from '@g/client/ClientsSectionSkeleton';

function ClientsSectionFC(props: { element: ClientsSectionComponent }) {
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
                Clients
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

export class ClientsSectionComponent extends ClientsSectionSkeleton {
    functionalComponent = ClientsSectionFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
