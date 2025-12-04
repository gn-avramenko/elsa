import { initStateSetters } from 'admin/src/common/component';
import { FormRemoteSelectSkeleton } from 'admin/src-gen/form/FormRemoteSelectSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { DebounceSelect } from 'admin/src/components/DebounceSelect';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from 'admin/src/common/extension';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function FormRemoteSelectFC(props: { element: FormRemoteSelectComponent }) {
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
            readonly={!!props.element.getReadonly() || viewMode}
        >
            <DebounceSelect
                size="middle"
                allowClear
                hasError={!!props.element.getValidation()}
                disabled={!!props.element.getReadonly() || viewMode}
                debounceTimeout={300}
                value={
                    props.element.getValue()
                        ? [
                              {
                                  key: props.element.getValue().id,
                                  label: shrinkName(
                                      props.element.getValue().displayName
                                  ),
                                  value: props.element.getValue().id,
                              },
                          ]
                        : []
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
                    minWidth: '300px',
                    padding: 0,
                    borderColor: props.element.getValidation() ? 'red' : undefined,
                }}
                onChange={(newValue) => {
                    props.element.resetValidation();
                    props.element.setValue(
                        newValue
                            ? {
                                  id: (newValue as any).value,
                                  displayName: (newValue as any).label,
                              }
                            : undefined
                    );
                    if (editor) {
                        editor.addTag('has-changes');
                    }
                }}
            />
        </FormElementWrapper>
    );
}

export class FormRemoteSelectComponent extends FormRemoteSelectSkeleton {
    functionalComponent = FormRemoteSelectFC;

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
