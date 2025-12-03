import { WebComponentWrapper } from 'admin/src/common/wrapper';
import { initStateSetters } from 'admin/src/common/component';
import { FormBooleanFieldSkeleton } from 'admin/src-gen/form/FormBooleanFieldSkeleton';

function FormBooleanFieldFC(props: { element: FormBooleanFieldComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            Custom
        </WebComponentWrapper>
    );
}

export class FormBooleanFieldComponent extends FormBooleanFieldSkeleton {
    functionalComponent = FormBooleanFieldFC;
}
