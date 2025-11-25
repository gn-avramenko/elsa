import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { FormContainerSkeleton } from 'admin/src-gen/form/FormContainerSkeleton';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from 'admin/src/common/extension';

function FormContainerFC(props: { element: FormContainerComponent }) {
    initStateSetters(props.element);
    const { breakpoint } = useBreakpoint(BREAKPOINTS);
    if (breakpoint === 'mobile') {
        return (
            <div style={{ width: '100%' }}>
                {props.element.children?.map((it) => {
                    return (it as BaseReactUiElement).createReactElement();
                })}
            </div>
        );
    }
    return (
        <table style={{ width: props.element.getWidth() ?? 500 }}>
            <tbody>
                {props.element.children?.map((it) => {
                    return (it as BaseReactUiElement).createReactElement();
                })}
            </tbody>
        </table>
    );
}

export class FormContainerComponent extends FormContainerSkeleton {
    functionalComponent = FormContainerFC;
}
