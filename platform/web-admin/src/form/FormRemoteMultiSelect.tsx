import { initStateSetters } from 'admin/src/common/component';
import { FormRemoteMultiSelectSkeleton } from 'admin/src-gen/form/FormRemoteMultiSelectSkeleton';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from 'admin/src/common/extension';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { DebounceSelect } from 'admin/src/components/DebounceSelect';

function FormRemoteMultiSelectFC(props: { element: FormRemoteMultiSelectComponent }) {
    initStateSetters(props.element);
    const { breakpoint } = useBreakpoint(BREAKPOINTS);
    const editor = useEditor();
    const viewMode = editor != null && !editor.hasTag('edit-mode');
    if (props.element.getHidden()) {
        return '' as any;
    }
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
            readonly={
                !!props.element.getReadonly() ||
                viewMode ||
                !!props.element.getReadonlyByAcl()
            }
        >
            <DebounceSelect
                size="middle"
                allowClear
                mode="multiple"
                hasError={!!props.element.getValidation()}
                disabled={
                    !!props.element.getReadonly() ||
                    viewMode ||
                    !!props.element.getReadonlyByAcl()
                }
                debounceTimeout={300}
                value={
                    props.element.getValue()?.map((it) => ({
                        key: it.id,
                        label: shrinkName(it.displayName),
                        value: it.id,
                    })) ?? []
                }
                fetchOptions={async (query) => {
                    const response = await props.element.doAutocomplete({
                        pattern: query,
                    });
                    return (response.items || []).map((it) => ({
                        key: it.id,
                        label: shrinkName(it.displayName),
                        value: it.id,
                    }));
                }}
                style={{
                    width: '100%',
                    padding: 0,
                    borderColor: props.element.getValidation() ? 'red' : undefined,
                }}
                onChange={(newValue) => {
                    props.element.resetValidation();
                    if (Array.isArray(newValue || [])) {
                        props.element.setValue(
                            (newValue as any[]).map((it) => ({
                                id: it.value,
                                displayName: it.label,
                            }))
                        );
                    }
                    if (editor) {
                        editor.addTag('has-changes');
                    }
                }}
            />
        </FormElementWrapper>
    );
}

export class FormRemoteMultiSelectComponent extends FormRemoteMultiSelectSkeleton {
    functionalComponent = FormRemoteMultiSelectFC;
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
