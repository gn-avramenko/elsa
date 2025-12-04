import { initStateSetters } from 'admin/src/common/component';
import { FormBooleanFieldSkeleton } from 'admin/src-gen/form/FormBooleanFieldSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { Switch, theme } from 'antd';

function FormBooleanFieldFC(props: { element: FormBooleanFieldComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <FormElementWrapper
            title={props.element.getTitle()}
            validation={props.element.getValidation()}
            hidden={props.element.getHidden()}
            readonly={!!props.element.getReadonly()}
        >
            <div
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                    borderWidth: '1px',
                    backgroundColor: token.colorBgContainer,
                    borderRadius: token.borderRadius,
                    borderColor: token.colorBorder,
                    borderStyle: 'solid',
                    paddingTop: '3px',
                    paddingBottom: '3px',
                }}
            >
                <div style={{ flexGrow: 1 }} />
                <Switch
                    style={{ flexGrow: 0 }}
                    disabled={props.element.getReadonly()}
                    value={props.element.getValue()}
                    onChange={(e) => {
                        props.element.resetValidation();
                        props.element.setValue(e);
                    }}
                />
            </div>
        </FormElementWrapper>
    );
}

export class FormBooleanFieldComponent extends FormBooleanFieldSkeleton {
    functionalComponent = FormBooleanFieldFC;
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
