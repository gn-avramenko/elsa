import { WebComponentWrapper } from 'admin/src/common/wrapper';
import { initStateSetters } from 'admin/src/common/component';
import { FormTextAreaSkeleton } from 'admin/src-gen/form/FormTextAreaSkeleton';

function FormTextAreaFC(props: { element: FormTextAreaComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            Custom
        </WebComponentWrapper>
    );
}

export class FormTextAreaComponent extends FormTextAreaSkeleton {
    functionalComponent = FormTextAreaFC;
}
