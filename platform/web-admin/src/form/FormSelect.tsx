import { initStateSetters } from 'admin/src/common/component';
import { FormSelectSkeleton } from 'admin/src-gen/form/FormSelectSkeleton';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from 'admin/src/common/extension';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { Select } from 'antd';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function FormSelectFC(props: { element: FormSelectComponent }) {
    initStateSetters(props.element);
    const { breakpoint } = useBreakpoint(BREAKPOINTS);
    const editor = useEditor();
    const viewMode = editor != null && !editor.hasTag('edit-mode');
    const shrinkName = (value: string) => {
        if (!value?.length) {
            return value;
        }
        if (breakpoint === 'mobile') {
            return value.length > 50 ? `${value.substring(0, 50)}...` : value;
        }
        return value.length > 100 ? `${value.substring(0, 100)}...` : value;
    };
    return (
        <FormElementWrapper
            title={props.element.getTitle()}
            validation={props.element.getValidation()}
            hidden={props.element.getHidden()}
            readonly={!!props.element.getReadonly() || viewMode}
        >
            <Select
                size="middle"
                allowClear
                disabled={!!props.element.getReadonly() || viewMode}
                className={props.element.getValidation() ? 'admin-form-error' : ''}
                options={(props.element.getOptions() || []).map((it) => ({
                    key: it.id,
                    label: shrinkName(it.displayName),
                    value: it.displayName,
                }))}
                value={props.element.getValue()}
                style={{
                    width: '100%',
                    minWidth: '300px',
                    padding: 0,
                }}
                onChange={(newValue) => {
                    props.element.resetValidation();
                    props.element.setValue(newValue);
                    if (editor) {
                        editor.addTag('has-changes');
                    }
                }}
            />
        </FormElementWrapper>
    );
}

export class FormSelectComponent extends FormSelectSkeleton {
    functionalComponent = FormSelectFC;
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
