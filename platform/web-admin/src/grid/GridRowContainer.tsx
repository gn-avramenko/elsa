import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { GridRowContainerSkeleton } from 'admin/src-gen/grid/GridRowContainerSkeleton';
import { Row } from 'antd';

function GridRowContainerFC(props: { element: GridRowContainerComponent }) {
    initStateSetters(props.element);
    return (
        <Row gutter={[8, 8]} style={{ width: 'calc(100% - 10px)' }}>
            {props.element.children?.map((ch) =>
                (ch as BaseReactUiElement).createReactElement()
            )}
        </Row>
    );
}

export class GridRowContainerComponent extends GridRowContainerSkeleton {
    functionalComponent = GridRowContainerFC;
}
