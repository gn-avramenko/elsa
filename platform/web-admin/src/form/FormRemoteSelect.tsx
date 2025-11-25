import { initStateSetters } from 'admin/src/common/component';
import { FormRemoteSelectSkeleton } from 'admin/src-gen/form/FormRemoteSelectSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { DebounceSelect } from 'admin/src/components/DebounceSelect';
import { theme } from 'antd';

function FormRemoteSelectFC(props: { element: FormRemoteSelectComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <FormElementWrapper title={props.element.getTitle()}>
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
                    padding: token.paddingXXS,
                    paddingRight: 0,
                }}
                onChange={(newValue) => {
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
        </FormElementWrapper>
    );
}

export class FormRemoteSelectComponent extends FormRemoteSelectSkeleton {
    functionalComponent = FormRemoteSelectFC;
}
