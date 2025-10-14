import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { DoctorsSectionSkeleton } from '@g/doctor/DoctorsSectionSkeleton';

function DoctorsSectionFC(props: { element: DoctorsSectionComponent }) {
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
                Doctors
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

export class DoctorsSectionComponent extends DoctorsSectionSkeleton {
    functionalComponent = DoctorsSectionFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
