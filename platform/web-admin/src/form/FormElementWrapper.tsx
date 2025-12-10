import { PropsWithChildren } from 'react';
import { Tooltip } from 'antd';
import { StyledFormElement } from 'admin/src/form/form-styled';
import { useDialog } from 'admin/src/mainFrame/MainFrame';

export function FormElementWrapper(
    props: PropsWithChildren<{
        title?: string;
        hidden?: boolean;
        validation?: string;
        readonly: boolean;
    }>
) {
    if (props.hidden) {
        return '';
    }
    const drawDiv = () => {
        return (
            <StyledFormElement>
                <div
                    className={`admin-form-wrapper-title${props.readonly ? ' disabled' : ''}`}
                >
                    {props.title}
                </div>
                {props.children}
            </StyledFormElement>
        );
    };
    const dialog = useDialog();
    if (props.validation && (!dialog || dialog.isOpen)) {
        return <Tooltip title={props.validation}>{drawDiv()}</Tooltip>;
    }
    return drawDiv();
}
