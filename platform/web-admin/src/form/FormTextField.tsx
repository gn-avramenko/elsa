import { initStateSetters } from 'admin/src/common/component';
import { FormTextFieldSkeleton } from 'admin/src-gen/form/FormTextFieldSkeleton';
import { Input } from 'antd';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function FormTextFieldFC(props: { element: FormTextFieldComponent }) {
    initStateSetters(props.element);
    const editor = useEditor();
    const viewMode = editor != null && !editor.hasTag('edit-mode');
    return (
        <FormElementWrapper
            title={props.element.getTitle()}
            validation={props.element.getValidation()}
            hidden={props.element.getHidden()}
            readonly={!!props.element.getReadonly() || viewMode}
        >
            <Input
                type="text"
                disabled={props.element.getReadonly() || viewMode}
                status={props.element.getValidation() ? 'error' : undefined}
                value={props.element.getValue()}
                onChange={(e) => {
                    props.element.resetValidation();
                    props.element.setValue(e.target.value);
                    if (editor) {
                        editor.addTag('has-changes');
                    }
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
