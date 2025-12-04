import { initStateSetters } from 'admin/src/common/component';
import { FormTextAreaSkeleton } from 'admin/src-gen/form/FormTextAreaSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import TextArea from 'antd/es/input/TextArea';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function FormTextAreaFC(props: { element: FormTextAreaComponent }) {
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
            <TextArea
                disabled={props.element.getReadonly() || viewMode}
                rows={5}
                style={{ resize: 'none' }}
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

export class FormTextAreaComponent extends FormTextAreaSkeleton {
    functionalComponent = FormTextAreaFC;
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
