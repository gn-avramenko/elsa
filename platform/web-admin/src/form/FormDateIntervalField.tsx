import { initStateSetters } from 'admin/src/common/component';
import { FormDateIntervalFieldSkeleton } from 'admin/src-gen/form/FormDateIntervalFieldSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { DatePicker } from 'antd';
import dayjs from 'dayjs';
import locale from 'antd/es/date-picker/locale/ru_RU';
import { adminWebPeerExt } from 'admin/src/common/extension';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function FormDateIntervalFieldFC(props: { element: FormDateIntervalFieldComponent }) {
    initStateSetters(props.element);
    const ruLang = adminWebPeerExt.language === 'ru';
    const editor = useEditor();
    const viewMode = editor != null && !editor.hasTag('edit-mode');
    const formatDate = (value: dayjs.Dayjs | null | undefined) => {
        if (!value) {
            return undefined;
        }
        return value.format('YYYY-MM-DD');
    };
    return (
        <FormElementWrapper
            title={props.element.getTitle()}
            validation={props.element.getValidation()}
            hidden={props.element.getHidden()}
            readonly={!!props.element.getReadonly() || viewMode}
        >
            <DatePicker.RangePicker
                style={{ width: '100%' }}
                locale={ruLang ? locale : undefined}
                disabled={props.element.getReadonly() || viewMode}
                status={props.element.getValidation() ? 'error' : undefined}
                value={[
                    props.element.getValue()?.startDate
                        ? dayjs(props.element.getValue().startDate)
                        : undefined,

                    props.element.getValue()?.endDate
                        ? dayjs(props.element.getValue().endDate)
                        : undefined,
                ]}
                onChange={(e) => {
                    props.element.resetValidation();
                    props.element.setValue({
                        startDate: formatDate(e && e[0]),
                        endDate: formatDate(e && e[0]),
                    });
                    if (editor) {
                        editor.addTag('has-changes');
                    }
                }}
            />
        </FormElementWrapper>
    );
}

export class FormDateIntervalFieldComponent extends FormDateIntervalFieldSkeleton {
    functionalComponent = FormDateIntervalFieldFC;
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
