import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { FormContainerSkeleton } from 'admin/src-gen/form/FormContainerSkeleton';

function FormContainerFC(props: { element: FormContainerComponent }) {
    initStateSetters(props.element);
    return (
        <div>
            {props.element.children!.map((it) => {
                return (
                    <div key={it.tag} style={{ marginTop: '9px' }}>
                        {(it as BaseReactUiElement).createReactElement()}
                    </div>
                );
            })}
        </div>
    );
}

export class FormContainerComponent extends FormContainerSkeleton {
    functionalComponent = FormContainerFC;
}
