import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { MeasurementsChartSkeleton } from '@g/main/MeasurementsChartSkeleton';

function MeasurementsChartFC(props: { element: MeasurementsChartComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <div
                className="webpeer-container-content"
                key="content"
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                }}
            >
                {props.element.tag}
            </div>
        </WebComponentWrapper>
    );
}

export class MeasurementsChartComponent extends MeasurementsChartSkeleton {
    functionalComponent = MeasurementsChartFC;
}
