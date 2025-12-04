import { initStateSetters } from 'admin/src/common/component';
import { FormDateIntervalFieldSkeleton } from 'admin/src-gen/form/FormDateIntervalFieldSkeleton';

function FormDateIntervalFieldFC(props: { element: FormDateIntervalFieldComponent }) {
    initStateSetters(props.element);
    return <div>Hello</div>;
}

export class FormDateIntervalFieldComponent extends FormDateIntervalFieldSkeleton {
    functionalComponent = FormDateIntervalFieldFC;
}
