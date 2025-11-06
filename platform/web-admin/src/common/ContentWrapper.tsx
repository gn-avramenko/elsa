import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { ContentWrapperSkeleton } from '@g/common/ContentWrapperSkeleton';

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
