import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { GroupEditorSkeleton } from 'admin/src-gen/common/GroupEditorSkeleton';
import { theme } from 'antd';
import {
    DownOutlined,
    MinusOutlined,
    PlusOutlined,
    UpOutlined,
} from '@ant-design/icons';

function GroupEditorFC(props: { element: GroupEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <>
            {props.element.children!.map((ch, idx, arr) => {
                return (
                    <div
                        key={`group-${idx}`}
                        style={{
                            width: '100%',
                            borderStyle: 'solid',
                            borderWidth: '1px',
                            borderColor: token.colorBorder,
                            position: 'relative',
                            borderRadius: token.borderRadius,
                            margin: token.margin,
                        }}
                    >
                        <div
                            style={{
                                backgroundColor: token.colorBgBase,
                                position: 'absolute',
                                top: '-10px',
                                right: '10px',
                                paddingLeft: '5px',
                                paddingRight: '5px',
                                zIndex: 10,
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
                        </div>
                        <div style={{ padding: token.paddingXS }}>
                            {(ch as BaseReactUiElement).createReactElement()}
                        </div>
                    </div>
                );
            })}
        </>
    );
}

export class GroupEditorComponent extends GroupEditorSkeleton {
    functionalComponent = GroupEditorFC;
}
