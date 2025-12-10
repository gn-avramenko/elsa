import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { ActionsEditorSkeleton } from 'admin/src-gen/common/ActionsEditorSkeleton';
import {
    DownOutlined,
    MinusOutlined,
    PlusOutlined,
    UpOutlined,
} from '@ant-design/icons';
import { theme } from 'antd';
import { ReactElement } from 'react';

function ActionsEditorFC(props: { element: ActionsEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    const divs = [] as ReactElement[];
    props.element.children?.forEach((ch, idx, arr) => {
        const item = ch as BaseReactUiElement;
        divs.push(
            <div key={`action-${ch.id}`} style={{ gridRow: idx + 1, gridColumn: 1 }}>
                {item.findByTag('action')?.createReactElement()}
            </div>
        );
        divs.push(
            <div key={`value-${ch.id}`} style={{ gridRow: idx + 1, gridColumn: 2 }}>
                {item.findByTag('value')?.createReactElement()}
            </div>
        );
        divs.push(
            <div
                key={`tools-${ch.id}`}
                style={{
                    width: 35,
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center',
                    gridRow: idx + 1,
                    gridColumn: 3,
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
                            props.element.isReadonly() || idx === arr.length - 1
                                ? token.colorTextDisabled
                                : undefined,
                    }}
                    onClick={() => {
                        if (!props.element.isReadonly() && idx !== arr.length - 1) {
                            props.element.sendMoveDown({
                                idx,
                            });
                        }
                    }}
                />
            </div>
        );
    });
    return (
        <div
            style={{
                width: 'calc(100% - 60px)',
                display: 'grid',
                gridTemplateColumns: '180px 1fr 30px',
                columnGap: 10,
                rowGap: 10,
            }}
        >
            {divs}
        </div>
    );
}

export class ActionsEditorComponent extends ActionsEditorSkeleton {
    functionalComponent = ActionsEditorFC;
}
