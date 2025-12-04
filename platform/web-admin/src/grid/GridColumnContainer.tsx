import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { GridColumnContainerSkeleton } from 'admin/src-gen/grid/GridColumnContainerSkeleton';
import { Col } from 'antd';

function GridColumnContainerFC(props: { element: GridColumnContainerComponent }) {
    initStateSetters(props.element);
    const getSpan = (value: number) => {
        return Math.round((24.0 * value) / 100);
    };
    return (
        <Col
            xs={getSpan(props.element.getSmallWidth() ?? 100)}
            sm={getSpan(props.element.getStandardWidth() ?? 100)}
            lg={getSpan(props.element.getLargeWidth() ?? 100)}
        >
            {props.element.children?.map((ch) =>
                (ch as BaseReactUiElement).createReactElement()
            )}
        </Col>
    );
}

export class GridColumnContainerComponent extends GridColumnContainerSkeleton {
    functionalComponent = GridColumnContainerFC;
}
