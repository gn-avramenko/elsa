import { initStateSetters } from 'admin/src/common/component';
import { FormTextAreaSkeleton } from 'admin/src-gen/form/FormTextAreaSkeleton';

function FormTextAreaFC(props: { element: FormTextAreaComponent }) {
    initStateSetters(props.element);
    return (
       <div>Hello</div>
    );
}

export class FormTextAreaComponent extends FormTextAreaSkeleton {
    functionalComponent = FormTextAreaFC;
}
