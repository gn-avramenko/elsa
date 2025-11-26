import { PropsWithChildren } from 'react';
import { Tooltip } from 'antd';
import { StyledFormElement } from 'admin/src/form/form-styled';

export function FormElementWrapper(
    props: PropsWithChildren<{ title?: string; validation?: string }>
) {
    const drawDiv = () => {
        return (
            <StyledFormElement>
                <div className="admin-form-wrapper-title">{props.title}</div>
                {props.children}
            </StyledFormElement>
        );
    };
    if (props.validation) {
        return <Tooltip title={props.validation}>{drawDiv()}</Tooltip>;
    }
    return drawDiv();
}
