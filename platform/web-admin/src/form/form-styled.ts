import styled from 'styled-components';
import { StyledComponentsTheme } from 'admin/src';

export const StyledFormElement: any = styled.div<{}>`
    position: relative;

    &:hover {
        .admin-form-wrapper-title {
            color: ${(props) =>
                (props.theme as StyledComponentsTheme).token.colorPrimary};
        }
    }

    .admin-form-wrapper-title {
        position: absolute;
        top: -9px;
        left: 10px;
        border-radius: 2px;
        padding-left: 3px;
        padding-right: 3px;
        color: ${({ theme }) =>
            (theme as StyledComponentsTheme).token.colorTextDescription};
        font-size: 12px;
        background-color: ${({ theme }) =>
            (theme as StyledComponentsTheme).token.colorBgBase};
        z-index: 30;
    }
    

    .admin-form-error {
        .ant-select-selector {
            border: 1px solid red !important;
        }
    }
`;
