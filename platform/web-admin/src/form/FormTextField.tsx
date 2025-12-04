import { initStateSetters } from 'admin/src/common/component';
import { FormTextFieldSkeleton } from 'admin/src-gen/form/FormTextFieldSkeleton';
import { Input } from 'antd';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';

function FormTextFieldFC(props: { element: FormTextFieldComponent }) {
    initStateSetters(props.element);
    return (
        <FormElementWrapper
            title={props.element.getTitle()}
            validation={props.element.getValidation()}
            hidden={props.element.getHidden()}
            readonly={!!props.element.getReadonly()}
        >
            <Input
                type="text"
                disabled={props.element.getReadonly()}
                status={props.element.getValidation() ? 'error' : undefined}
                value={props.element.getValue()}
                onChange={(e) => {
                    props.element.resetValidation();
                    props.element.setValue(e.target.value);
                }}
                // onChange={handleCommentLength}
            />
        </FormElementWrapper>
    );
}

export class FormTextFieldComponent extends FormTextFieldSkeleton {
    functionalComponent = FormTextFieldFC;
    resetValidation() {
        this.stateSetters.get('validation')!(null);
        this.sendCommand(
            'pc',
            {
                pn: 'validation',
                pv: null,
            },
            true
        );
    }
}
