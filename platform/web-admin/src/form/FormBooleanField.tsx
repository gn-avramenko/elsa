import { initStateSetters } from 'admin/src/common/component';
import { FormBooleanFieldSkeleton } from 'admin/src-gen/form/FormBooleanFieldSkeleton';

function FormBooleanFieldFC(props: { element: FormBooleanFieldComponent }) {
    initStateSetters(props.element);
    return <div>Hello</div>;
}

export class FormBooleanFieldComponent extends FormBooleanFieldSkeleton {
    functionalComponent = FormBooleanFieldFC;
}
