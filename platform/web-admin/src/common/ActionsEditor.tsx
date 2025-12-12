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
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function ActionsEditorFC(props: { element: ActionsEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    const divs = [] as ReactElement[];
    const editor = useEditor();
    const viewMode = editor != null && !editor.hasTag('edit-mode');
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
                    width: 90,
                    display: 'flex',
                    flexDirection: 'row',
                    alignSelf: 'start',
                    gridRow: idx + 1,
                    gridColumn: 3,
                }}
            >
                <PlusOutlined
                    style={{
                        paddingLeft: token.paddingXS,
                        color: viewMode ? token.colorTextDisabled : undefined,
                    }}
                    onClick={() => {
                        if (!viewMode) {
                            props.element.sendAdd({
                                idx,
                            });
                            if (editor) {
                                editor.addTag('has-changes');
                            }
                        }
                    }}
                />
                <MinusOutlined
                    style={{
                        paddingLeft: token.paddingXS,
                        color: viewMode ? token.colorTextDisabled : undefined,
                    }}
                    onClick={() => {
                        if (!viewMode) {
                            props.element.sendDelete({
                                idx,
                            });
                            if (editor) {
                                editor.addTag('has-changes');
                            }
                        }
                    }}
                />
                <UpOutlined
                    style={{
                        paddingLeft: token.paddingXS,
                        color:
                            viewMode || idx === 0 ? token.colorTextDisabled : undefined,
                    }}
                    onClick={() => {
                        if (!viewMode && idx !== 0) {
                            props.element.sendMoveUp({
                                idx,
                            });
                            if (editor) {
                                editor.addTag('has-changes');
                            }
                        }
                    }}
                />
                <DownOutlined
                    style={{
                        paddingLeft: token.paddingXS,
                        color:
                            viewMode || idx === arr.length - 1
                                ? token.colorTextDisabled
                                : undefined,
                    }}
                    onClick={() => {
                        if (!viewMode && idx !== arr.length - 1) {
                            props.element.sendMoveDown({
                                idx,
                            });
                            if (editor) {
                                editor.addTag('has-changes');
                            }
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
