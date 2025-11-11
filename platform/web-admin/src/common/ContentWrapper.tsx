import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { ContentWrapperSkeleton } from 'admin/src-gen/common/ContentWrapperSkeleton';

function ContentWrapperFC(props: { element: ContentWrapperComponent }) {
    initStateSetters(props.element);
    return (
        <>
            {(props.element.children || []).map((it) => {
                return (it as BaseReactUiElement).createReactElement();
            })}
        </>
    );
}

export class ContentWrapperComponent extends ContentWrapperSkeleton {
    functionalComponent = ContentWrapperFC;
}
