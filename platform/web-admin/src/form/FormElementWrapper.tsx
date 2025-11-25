import { PropsWithChildren } from 'react';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from 'admin/src/common/extension';
import { theme } from 'antd';

export function FormElementWrapper(props: PropsWithChildren<{ title?: string }>) {
    const { breakpoint } = useBreakpoint(BREAKPOINTS);
    const { token } = theme.useToken();
    if (breakpoint === 'mobile') {
        return (
            <div
                style={{
                    width: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'flex-start',
                }}
            >
                <div style={{ padding: token.paddingXXS }}>{props.title ?? ''}</div>
                <div style={{ width: '100%' }}>{props.children}</div>
            </div>
        );
    }
    return (
        <tr>
            <td>{props.title}</td>
            <td style={{ padding: 0 }}>{props.children}</td>
        </tr>
    );
}
