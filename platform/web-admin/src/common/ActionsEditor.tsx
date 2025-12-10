import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { ActionsEditorSkeleton } from 'admin/src-gen/common/ActionsEditorSkeleton';
import {
    DownOutlined,
    MinusOutlined,
    PlusOutlined,
    UpOutlined,
} from '@ant-design/icons';
import { theme } from 'antd';

function ActionsEditorFC(props: { element: ActionsEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <table style={{ width: '100%' }}>
            <tbody>
                {props.element.children?.map((ch, idx, arr) => {
                    const item = ch as BaseReactUiElement;
                    return (
                        <tr key={`row-${idx}`}>
                            <td style={{ minWidth: 150 }}>
                                {item.findByTag('action')?.createReactElement()}
                            </td>
                            <td>{item.findByTag('value')?.createReactElement()}</td>
                            <td
                                style={{
                                    width: 35,
                                    display: 'flex',
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                }}
                            >
                                <PlusOutlined
                                    style={{
                                        paddingLeft: token.paddingXS,
                                        color: props.element.isReadonly()
                                            ? token.colorTextDisabled
                                            : undefined,
                                    }}
                                    onClick={() => {
                                        if (!props.element.isReadonly()) {
                                            props.element.sendAdd({
                                                idx,
                                            });
                                        }
                                    }}
                                />
                                <MinusOutlined
                                    style={{
                                        paddingLeft: token.paddingXS,
                                        color: props.element.isReadonly()
                                            ? token.colorTextDisabled
                                            : undefined,
                                    }}
                                    onClick={() => {
                                        if (!props.element.isReadonly()) {
                                            props.element.sendDelete({
                                                idx,
                                            });
                                        }
                                    }}
                                />
                                <UpOutlined
                                    style={{
                                        paddingLeft: token.paddingXS,
                                        color:
                                            props.element.isReadonly() || idx === 0
                                                ? token.colorTextDisabled
                                                : undefined,
                                    }}
                                    onClick={() => {
                                        if (!props.element.isReadonly() && idx !== 0) {
                                            props.element.sendMoveUp({
                                                idx,
                                            });
                                        }
                                    }}
                                />
                                <DownOutlined
                                    style={{
                                        paddingLeft: token.paddingXS,
                                        color:
                                            props.element.isReadonly() ||
                                            idx === arr.length - 1
                                                ? token.colorTextDisabled
                                                : undefined,
                                    }}
                                    onClick={() => {
                                        if (
                                            !props.element.isReadonly() &&
                                            idx !== arr.length - 1
                                        ) {
                                            props.element.sendMoveDown({
                                                idx,
                                            });
                                        }
                                    }}
                                />
                            </td>
                        </tr>
                    );
                })}
            </tbody>
        </table>
    );
}

export class ActionsEditorComponent extends ActionsEditorSkeleton {
    functionalComponent = ActionsEditorFC;
}
