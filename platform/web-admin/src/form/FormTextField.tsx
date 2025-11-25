import { initStateSetters } from 'admin/src/common/component';
import { FormTextFieldSkeleton } from 'admin/src-gen/form/FormTextFieldSkeleton';
import { Input } from 'antd';

function FormTextFieldFC(props: { element: FormTextFieldComponent }) {
    initStateSetters(props.element);
    return <Input type="text" />;
}

export class FormTextFieldComponent extends FormTextFieldSkeleton {
    functionalComponent = FormTextFieldFC;
}
