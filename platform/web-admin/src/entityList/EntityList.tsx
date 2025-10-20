import { initStateSetters } from '@/common/component';
import { EntityListSkeleton } from '@g/entityList/EntityListSkeleton';
import useBreakpoint from 'use-breakpoint';
import { BREAKPOINTS } from '@/common/extension';
import React, { useEffect, useState } from 'react';
import { ColumnDescription } from '@g/entityList/ColumnDescription';
import { Table, theme } from 'antd';
import { onVisible } from '@/common/utils';
import { RowData } from '@g/entityList/RowData';
import { Sort } from '@g/entityList/Sort';
import Input from 'antd/es/input/Input';
import { debounce } from '@/common/debounce';
import { generateUUID } from 'webpeer-core';

function EntityListFC(props: { element: EntityListComponent }) {
    initStateSetters(props.element);
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState<RowData[]>([]);
    const [sort, setSort] = useState<Sort>(props.element.getDefaultSort());
    const [hasMore, setHasMore] = useState(false);
    const parentRef = React.createRef() as any;
    const reqId = React.createRef<string>();
    const { token } = theme.useToken();
    const [tableHeight, setTableHeight] = useState<number | undefined>(
        parentRef?.current?.clientHeight
    );
    const [freeText, setFreeText] = useState<string>('');
    const { breakpoint } = useBreakpoint(BREAKPOINTS, 'desktop');
    const [tableWidth, setTableWidth] = useState<number | undefined>(1000);
    const [augmented, setAugmented] = useState(false);
    const debouncedSearch = debounce((freeText?: string) => {
        updateData(freeText);
    }, 300);
    const updateData = async (freeText?: string) => {
        const id = generateUUID();
        reqId.current = id;
        setLoading(true);
        const result = await props.element.doGetData({
            sort,
            breakPoint: breakpoint === 'mobile' ? 'MOBILE' : 'DESKTOP',
            freeText,
            limit: props.element.getLimit(),
        });
        if (reqId.current !== id) {
            return;
        }
        setHasMore(result.hasMore);
        setData(result.data || []);
        setLoading(false);
    };
    props.element.processRefreshData = () => {
        updateData(freeText);
    };
    useEffect(() => {
        if (!parentRef?.current) return;
        setTableHeight(parentRef.current.clientHeight - 70);
        setTableWidth(parentRef.current.clientWidth);
    }, [parentRef]);
    useEffect(() => {
        updateData();
    }, [props.element]);
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
    return (
        <div ref={parentRef as any} key={props.element.id} style={{}}>
            <div style={{ display: 'flex', flexDirection: 'row' }}>
                <div style={{ flexGrow: 1 }} />
                <div style={{ flexGrow: 0, padding: token.paddingSM }}>
                    <Input
                        key="search-field"
                        autoFocus
                        value={freeText}
                        allowClear
                        onChange={(e) => {
                            setFreeText(e.target.value);
                            debouncedSearch(e.target.value);
                        }}
                    />
                </div>
            </div>
            <Table
                size="small"
                key={props.element.id}
                loading={loading}
                dataSource={data}
                pagination={false}
                rowKey="id"
                showSorterTooltip={false}
                rowSelection={{
                    type: 'checkbox',
                    hideSelectAll: false,
                }}
                style={{ width: tableWidth }}
                scroll={{
                    y: data.length > 10 ? tableHeight || 200 : undefined,
                }}
                columns={props.element.getColumns().map((c) => ({
                    title: c.title,
                    align: getAlignment(c),
                    dataIndex: c.id,
                    sorter: c.sortable,
                    sortOrder:
                        sort.field === c.id
                            ? sort.order === 'DESC'
                                ? 'descend'
                                : 'ascend'
                            : undefined,
                    width: c.width,
                    render: (_value, record: RowData) => {
                        return renderContent((record.fields as any)[c.id], c);
                    },
                }))}
                onScroll={() => {
                    const elms = document.getElementsByClassName('the-last-row');
                    if (elms.length) {
                        const lastRow = elms[elms.length - 1];
                        if (!augmented) {
                            setAugmented(true);
                            onVisible(lastRow, () => {
                                if (hasMore) {
                                    setTimeout(async () => {
                                        await updateData();
                                        setTimeout(() => setAugmented(false), 10);
                                    });
                                }
                            });
                        }
                    }
                }}
                rowClassName={(_record, index) => {
                    const length = data.length as number;
                    if (length && index === length - 1) {
                        return 'the-last-row';
                    }
                    return '';
                }}
                onChange={(_pagination, _filter, sorter: any) => {
                    setTimeout(async () => {
                        setLoading(true);
                        setSort({
                            field: sorter.field ?? sort.field,
                            order: sorter.order === 'descend' ? 'DESC' : 'ASC',
                        });
                        setTimeout(async () => {
                            await updateData();
                            setAugmented(false);
                        }, 10);
                    });
                }}
            />
        </div>
    );
}

export class EntityListComponent extends EntityListSkeleton {
    processRefreshData(): void {
        throw new Error('Method not implemented.');
    }
    functionalComponent = EntityListFC;
}
