import { initStateSetters } from 'admin/src/common/component';
import { FormRemoteSelectSkeleton } from 'admin/src-gen/form/FormRemoteSelectSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { DebounceSelect } from 'admin/src/components/DebounceSelect';
import { Tooltip } from 'antd';

function FormRemoteSelectFC(props: { element: FormRemoteSelectComponent }) {
    initStateSetters(props.element);
    const drawSelect = () => {
        return (
            <DebounceSelect
                size="middle"
                allowClear
                debounceTimeout={300}
                value={
                    props.element.getValue()
                        ? [
                              {
                                  key: props.element.getValue().id,
                                  label: props.element.getValue().displayName,
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
                        label: it.displayName,
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
                }}
            />
        );
    };
    const validation = props.element.getValidation();
    return (
        <FormElementWrapper title={props.element.getTitle()}>
            {validation ? (
                <Tooltip title={validation}>
                    <div style={{ border: '1px solid red' }}>{drawSelect()}</div>
                </Tooltip>
            ) : (
                drawSelect()
            )}
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
