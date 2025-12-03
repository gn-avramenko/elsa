import { WebComponentWrapper } from 'admin/src/common/wrapper';
import { initStateSetters } from 'admin/src/common/component';
import { FormDateIntervalFieldSkeleton } from 'admin/src-gen/form/FormDateIntervalFieldSkeleton';

function FormDateIntervalFieldFC(props: { element: FormDateIntervalFieldComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            Custom
        </WebComponentWrapper>
    );
}

export class FormDateIntervalFieldComponent extends FormDateIntervalFieldSkeleton {
    functionalComponent = FormDateIntervalFieldFC;
}
