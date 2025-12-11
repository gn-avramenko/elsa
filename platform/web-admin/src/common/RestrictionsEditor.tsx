import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { RestrictionsEditorSkeleton } from 'admin/src-gen/common/RestrictionsEditorSkeleton';
import { Dropdown, theme } from 'antd';
import {
    DownOutlined,
    MinusOutlined,
    PlusOutlined,
    UpOutlined,
} from '@ant-design/icons';
import { BaseUiElement } from 'webpeer-core';
import { RestrictionEditorComponent } from 'admin/src/common/RestrictionEditor';
import { ReactElement } from 'react';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';
import { MenuProps } from 'antd/lib';
import { range } from 'lodash';

function RestrictionsEditorFC(props: { element: RestrictionsEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    const editor = useEditor();
    const viewMode = editor != null && !editor.hasTag('edit-mode');
    const calculateMaxColumnsCount = (
        startingValue: number,
        nestedCount: number,
        children: BaseUiElement[]
    ) => {
        let value = startingValue;
        children.forEach((it) => {
            const ed = it as RestrictionEditorComponent;
            if (ed.getRestrictionType() === 'SIMPLE') {
                if (value < nestedCount + 4) {
                    value = nestedCount + 4;
                }
                return;
            }
            const newCount = calculateMaxColumnsCount(
                value,
                nestedCount + 1,
                ed.children!
            );
            if (newCount > value) {
                value = newCount;
            }
        });
        return value;
    };
    const maxColumnCount = calculateMaxColumnsCount(4, 0, props.element.children!);

    let columns = '';
    for (let n of range(1, maxColumnCount + 1)) {
        if (n > 1) {
            columns += ' ';
        }
        if (n === maxColumnCount) {
            columns += '90px';
            continue;
        }
        if (n === maxColumnCount - 1) {
            columns += '5fr';
            continue;
        }
        if (n === 1 && maxColumnCount > 4) {
            columns += '100px';
            continue;
        }

        columns += '1fr';
    }

    const rows = [] as ReactElement[][];
    const collectElements = (
        elements: BaseUiElement[],
        nestedCount: number,
        rowIdx: number,
        coll: ReactElement[][]
    ) => {
        let result = rowIdx;
        elements.forEach((it, idx, arr) => {
            const ch = it as RestrictionEditorComponent;
            const row = [] as ReactElement[];
            if (ch.getRestrictionType() === 'SIMPLE') {
                row.push(
                    <div
                        key={`property-${ch.id}`}
                        style={{
                            gridRow: result + 1,
                            gridColumn: `${nestedCount + 1}/${maxColumnCount - 2}`,
                        }}
                    >
                        {ch.findByTag('property')?.createReactElement()}
                    </div>
                );
                row.push(
                    <div
                        key={`condition-${ch.id}`}
                        style={{
                            gridRow: result + 1,
                            gridColumn: maxColumnCount - 2,
                        }}
                    >
                        {ch.findByTag('condition')?.createReactElement()}
                    </div>
                );
                row.push(
                    <div
                        key={`value-${ch.id}`}
                        style={{
                            gridRow: result + 1,
                            gridColumn: maxColumnCount - 1,
                        }}
                    >
                        {ch.findByTag('value')?.createReactElement()}
                    </div>
                );
                const items: MenuProps['items'] = [
                    {
                        key: 'SIMPLE',
                        label: props.element.getL10nSimple(),
                    },
                    {
                        key: 'OR',
                        label: props.element.getL10nOr(),
                    },
                    {
                        key: 'AND',
                        label: props.element.getL10nAnd(),
                    },
                    {
                        key: 'NOT',
                        label: props.element.getL10nNot(),
                    },
                ];
                row.push(
                    <div
                        key={`tools-${ch.id}`}
                        style={{
                            width: 35,
                            display: 'flex',
                            flexDirection: 'row',
                            alignSelf: 'start',
                            gridRow: result + 1,
                            gridColumn: maxColumnCount,
                        }}
                    >
                        <Dropdown
                            menu={{
                                items,
                                onClick: (info) => {
                                    props.element.sendAdd({
                                        restrictionType: info.key,
                                        elementId: ch.id,
                                    });
                                },
                            }}
                        >
                            <PlusOutlined
                                style={{
                                    paddingLeft: token.paddingXS,
                                    color: viewMode
                                        ? token.colorTextDisabled
                                        : undefined,
                                }}
                                onClick={(e) => {
                                    e.preventDefault();
                                }}
                            />
                        </Dropdown>
                        <MinusOutlined
                            style={{
                                paddingLeft: token.paddingXS,
                                color: viewMode ? token.colorTextDisabled : undefined,
                            }}
                            onClick={() => {
                                if (!viewMode) {
                                    props.element.sendDelete({
                                        elementId: ch.id,
                                    });
                                }
                            }}
                        />
                        <UpOutlined
                            style={{
                                paddingLeft: token.paddingXS,
                                color:
                                    viewMode || idx === 0
                                        ? token.colorTextDisabled
                                        : undefined,
                            }}
                            onClick={() => {
                                if (!viewMode && idx > 0) {
                                    props.element.sendMoveUp({
                                        elementId: ch.id,
                                    });
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
                                        elementId: ch.id,
                                    });
                                }
                            }}
                        />
                    </div>
                );
                result += 1;
                coll.push(row);
            } else {
                const children = ch.children as BaseReactUiElement[];
                const items: MenuProps['items'] = [
                    {
                        key: 'SIMPLE',
                        label: props.element.getL10nSimple(),
                    },
                    {
                        key: 'OR',
                        label: props.element.getL10nOr(),
                    },
                    {
                        key: 'AND',
                        label: props.element.getL10nAnd(),
                    },
                    {
                        key: 'NOT',
                        label: props.element.getL10nNot(),
                    },
                ];
                row.push(
                    <div
                        key={`condition-type-${ch.id}`}
                        style={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            gridRow: `${result + 1}/${result + 1 + children.length}`,
                            gridColumn: `${nestedCount + 1}/${maxColumnCount - 3}`,
                            borderStyle: 'solid',
                            borderWidth: '1px',
                            borderRadius: token.borderRadius,
                            borderColor: token.colorBorder,
                            paddingTop: token.paddingXS,
                            paddingBottom: token.paddingXS,
                        }}
                    >
                        <div
                            key={`tools-${ch.id}`}
                            style={{
                                width: 35,
                                display: 'flex',
                                flexDirection: 'row',
                                alignSelf: 'start',
                                gridRow: rowIdx + idx + 1,
                                gridColumn: maxColumnCount,
                            }}
                        >
                            <Dropdown
                                menu={{
                                    items,
                                    onClick: (info) => {
                                        props.element.sendAdd({
                                            restrictionType: info.key,
                                            elementId: ch.id,
                                        });
                                    },
                                }}
                            >
                                <PlusOutlined
                                    style={{
                                        paddingLeft: token.paddingXS,
                                        color: viewMode
                                            ? token.colorTextDisabled
                                            : undefined,
                                    }}
                                    onClick={(e) => {
                                        e.preventDefault();
                                    }}
                                />
                            </Dropdown>
                            <MinusOutlined
                                style={{
                                    paddingLeft: token.paddingXS,
                                    color: viewMode
                                        ? token.colorTextDisabled
                                        : undefined,
                                }}
                                onClick={() => {
                                    if (!viewMode) {
                                        props.element.sendDelete({
                                            elementId: ch.id,
                                        });
                                    }
                                }}
                            />
                            <UpOutlined
                                style={{
                                    paddingLeft: token.paddingXS,
                                    color:
                                        viewMode || idx === 0
                                            ? token.colorTextDisabled
                                            : undefined,
                                }}
                                onClick={() => {
                                    if (!viewMode && idx > 0) {
                                        props.element.sendMoveUp({
                                            elementId: ch.id,
                                        });
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
                                            elementId: ch.id,
                                        });
                                    }
                                }}
                            />
                        </div>
                        <div>
                            {ch.getRestrictionType() === 'NOT'
                                ? props.element.getL10nNot()
                                : ''}
                            {ch.getRestrictionType() === 'AND'
                                ? props.element.getL10nAnd()
                                : ''}
                            {ch.getRestrictionType() === 'OR'
                                ? props.element.getL10nOr()
                                : ''}
                        </div>
                    </div>
                );
                const nestedRows = [] as ReactElement[][];
                result = collectElements(children, nestedCount + 1, result, nestedRows);
                nestedRows[0].forEach((ne) => row.push(ne));
                coll.push(row);
                for (let n = 1; n < nestedRows.length; n++) {
                    coll.push(nestedRows[n]);
                }
            }
        });
        return result;
    };
    collectElements(props.element.children!, 0, 0, rows);
    const elements = [] as ReactElement[];
    rows.forEach((row) => row.forEach((i) => elements.push(i)));
    return (
        <div
            style={{
                width: 'calc(100% - 5px)',
                display: 'grid',
                gridTemplateColumns: columns,
                columnGap: 10,
                rowGap: 10,
            }}
        >
            {elements}
        </div>
    );
}

export class RestrictionsEditorComponent extends RestrictionsEditorSkeleton {
    functionalComponent = RestrictionsEditorFC;
}
