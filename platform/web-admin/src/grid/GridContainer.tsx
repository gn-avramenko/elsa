import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { GridContainerSkeleton } from 'admin/src-gen/grid/GridContainerSkeleton';

function GridContainerFC(props: { element: GridContainerComponent }) {
    initStateSetters(props.element);
    return (
        <>
            {props.element.children?.map((ch) =>
                (ch as BaseReactUiElement).createReactElement()
            )}
        </>
    );
}

export class GridContainerComponent extends GridContainerSkeleton {
    functionalComponent = GridContainerFC;
}
