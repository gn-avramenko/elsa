import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { EntityListSkeleton } from '@g/entityList/EntityListSkeleton';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from '@/common/extension';
import React, { useEffect, useState } from 'react';
import { ColumnDescription } from '@g/entityList/ColumnDescription';
import { Button, Drawer, Table, theme } from 'antd';
import { onVisible } from '@/common/utils';
import { RowData } from '@g/entityList/RowData';
import { BreakPoint } from '@g/common/BreakPoint';
import { ColumnsType } from 'antd/es/table';
import { EyeOutlined, FilterOutlined } from '@ant-design/icons';

function EntityListFC(props: { element: EntityListComponent }) {
    initStateSetters(props.element);
    const parentRef = React.createRef() as any;
    const { token } = theme.useToken();
    const [tableHeight, setTableHeight] = useState<number | undefined>(
        parentRef?.current?.clientHeight
    );
    const { breakpoint } = useBreakpoint(BREAKPOINTS, 'desktop');
    props.element.breakPoint = breakpoint as BreakPoint;
    const [tableWidth, setTableWidth] = useState<number | undefined>(1000);
    const [augmented, setAugmented] = useState(false);
    const [drawerVisible, setDrawerVisible] = useState(false);
    props.element.augmentedSetter = setAugmented;
    useEffect(() => {
        if (!parentRef?.current) return;
        setTableHeight(parentRef.current.clientHeight - 70);
        setTableWidth(parentRef.current.clientWidth);
    }, [parentRef]);
    const renderContent = (value: any | undefined, cd: ColumnDescription) => {
        if (value === undefined || value === null) {
            return '';
        }
        switch (cd.columnType) {
            default:
                return value.toString();
        }
    };
    const getAlignment = (cd: ColumnDescription) => {
        switch (cd.columnType) {
            case 'NUMBER':
                return 'right';
            default:
                return 'left';
        }
    };
    const columns: ColumnsType<RowData> = props.element.getColumns().map((c) => ({
        title: c.title,
        align: getAlignment(c),
        dataIndex: c.id,
        sorter: c.sortable,
        sortOrder:
            props.element.getSort().field === c.id
                ? props.element.getSort().order === 'DESC'
                    ? 'descend'
                    : 'ascend'
                : undefined,
        width: c.width,
        render: (_value, record: RowData) => {
            return renderContent((record.fields as any)[c.id], c);
        },
    }));
    columns.push({
        title: '',
        sorter: false,
        width: '60px',
        render: (_value, record) => {
            return (
                <Button
                    type="primary"
                    onClick={() => {
                        props.element.sendDoubleClick({
                            id: record.id,
                        });
                    }}
                >
                    <EyeOutlined />
                </Button>
            );
        },
    });
    const filters = (props.element.findByTag('filters')?.findByTag('filters')
        ?.children || []) as BaseReactUiElement[];
    const buttons = (props.element.findByTag('filters')?.findByTag('buttons')
        ?.children || []) as BaseReactUiElement[];
    return (
        <div
            ref={parentRef as any}
            key={props.element.id}
            style={{ height: 'calc(100% - 25px)' }}
        >
            <div style={{ display: 'flex', flexDirection: 'row' }}>
                <div key="tools" style={{ flexGrow: 1, padding: token.paddingSM }}>
                    {props.element
                        .findByTag('tools')
                        .children?.map((it) =>
                            (it as BaseReactUiElement).createReactElement()
                        )}
                </div>
                <div
                    key="glue1"
                    style={{ flexGrow: 1, padding: token.paddingSM }}
                ></div>
                <div
                    key="searchField"
                    style={{ flexGrow: 0, padding: token.paddingSM, paddingRight: 5 }}
                >
                    {props.element.findByTag('searchField').createReactElement()}
                </div>
                {filters.length ? (
                    <div
                        key="showFilters"
                        style={{
                            flexGrow: 0,
                            padding: token.paddingSM,
                            paddingLeft: 5,
                            paddingRight: 5,
                        }}
                    >
                        <Button
                            style={{ padding: 0 }}
                            id="showFilter"
                            onClick={() => setDrawerVisible(!drawerVisible)}
                            icon={<FilterOutlined />}
                        ></Button>
                    </div>
                ) : null}
            </div>
            {filters.length ? (
                <Drawer
                    title={props.element.getFiltersTitle()}
                    styles={{
                        header: { padding: 5 },
                        content: { padding: 0 },
                        body: { padding: 5 },
                    }}
                    open={drawerVisible}
                    closable={{ 'aria-label': 'Close Button' }}
                    onClose={() => setDrawerVisible(false)}
                >
                    <div
                        key="filters"
                        style={{
                            display: 'flex',
                            flexDirection: 'column',
                            height: '100%',
                        }}
                    >
                        <div
                            key="filters"
                            style={{
                                flexGrow: 1,
                                overflowY: 'auto',
                            }}
                        >
                            {filters.map((it) => it.createReactElement())}
                        </div>
                        <div
                            key="buttons"
                            style={{
                                display: 'flex',
                                flexDirection: 'row',
                                flexGrow: 0,
                            }}
                        >
                            <div key="glue" style={{ flexGrow: 1 }} />
                            {buttons.map((it) => {
                                return (
                                    <div
                                        key={it.id}
                                        style={{ flexGrow: 0, padding: 5 }}
                                    >
                                        {' '}
                                        {it.createReactElement()}
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                </Drawer>
            ) : null}
            <Table
                size="small"
                key={props.element.id}
                loading={props.element.isLoading()}
                dataSource={props.element.getData()}
                pagination={false}
                rowKey="id"
                showSorterTooltip={false}
                onRow={(record) => {
                    return {
                        onDoubleClick: () => {
                            props.element.sendDoubleClick({
                                id: record.id,
                            });
                        },
                    };
                }}
                rowSelection={
                    props.element.getSelectionType() === 'NONE'
                        ? undefined
                        : {
                              onChange: (keys) => {
                                  props.element.setSelectedItems(keys as string[]);
                              },
                              type:
                                  props.element.getSelectionType() === 'SINGLE'
                                      ? 'radio'
                                      : 'checkbox',
                              hideSelectAll: false,
                          }
                }
                style={{ width: tableWidth }}
                scroll={{
                    y:
                        props.element.getData().length > 10
                            ? tableHeight || 200
                            : undefined,
                }}
                columns={columns}
                onScroll={() => {
                    const elms = document.getElementsByClassName('the-last-row');
                    if (elms.length) {
                        const lastRow = elms[elms.length - 1];
                        if (!augmented) {
                            setAugmented(true);
                            onVisible(lastRow, () => {
                                if (props.element.isHasMore()) {
                                    props.element.sendLoadMore();
                                }
                            });
                        }
                    }
                }}
                rowClassName={(_record, index) => {
                    const length = props.element.getData().length as number;
                    if (length && index === length - 1) {
                        return 'the-last-row';
                    }
                    return '';
                }}
                onChange={(_pagination, _filter, sorter: any) => {
                    setTimeout(() => {
                        props.element.sendChangeSort({
                            field: sorter.field ?? props.element.getSort().field,
                            sortOrder: sorter.order === 'descend' ? 'DESC' : 'ASC',
                        });
                    });
                }}
            />
        </div>
    );
}

export class EntityListComponent extends EntityListSkeleton {
    breakPoint: BreakPoint = 'DESKTOP';
    augmentedSetter: React.Dispatch<React.SetStateAction<boolean>> = (_value) => {};

    setSelectedItems(items: string[]): void {
        this.sendPropertyChange('selectedItems', items, true);
    }

    processRefreshData(): void {
        this.sendRefreshData({
            breakPoint: this.breakPoint,
        });
    }
    protected updatePropertyValue(pn: string, pv: any) {
        if (pn === 'data') {
            super.updatePropertyValue(pn, pv);
            setTimeout(() => this.augmentedSetter(false), 10);
            return;
        }
        super.updatePropertyValue(pn, pv);
    }
    functionalComponent = EntityListFC;
}
