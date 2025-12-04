import { initStateSetters } from 'admin/src/common/component';
import { FormDateIntervalFieldSkeleton } from 'admin/src-gen/form/FormDateIntervalFieldSkeleton';
import { FormElementWrapper } from 'admin/src/form/FormElementWrapper';
import { DatePicker } from 'antd';
import dayjs from 'dayjs';
import locale from 'antd/es/date-picker/locale/ru_RU';
import { adminWebPeerExt } from 'admin/src/common/extension';

function FormDateIntervalFieldFC(props: { element: FormDateIntervalFieldComponent }) {
    initStateSetters(props.element);
    const ruLang = adminWebPeerExt.language === 'ru';
    return (
        <FormElementWrapper
            title={props.element.getTitle()}
            validation={props.element.getValidation()}
            hidden={props.element.getHidden()}
            readonly={!!props.element.getReadonly()}
        >
            <DatePicker.RangePicker
                style={{ width: '100%' }}
                locale={ruLang ? locale : undefined}
                disabled={props.element.getReadonly()}
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
                        startDate: (e && e[0]?.toISOString()) ?? undefined,
                        endDate: (e && e[1]?.toISOString()) ?? undefined,
                    });
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
