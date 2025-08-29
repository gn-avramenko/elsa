import {
    BaseReactUiElement,
    ReactUiElementFactory,
    TableAction,
    TestColumnDescription,
    TestOption,
    TestSort,
} from './common-component';
import React, { useEffect, useState } from 'react';
import { DetailsModal, useModal } from './modal';
import './scaffold-styles.css';

export type EntityListColumnDescription = TestColumnDescription & {};

export type TestOrganizationEntry = {
    id: string;
    name?: string;
    address?: string;
    country?: TestOption;
    contacts?: string;
};
function EntityListComponent(props: { element: TestEntityList }) {
    for (const prop of props.element.state.keys()) {
        const [value, setValue] = useState(props.element.state.get(prop));
        props.element.state.set(prop, value);
        props.element.stateSetters.set(prop, setValue);
    }
    useEffect(() => {
        props.element.state.forEach((value, key) => {
            props.element.stateSetters.get(key)?.(value);
        });
    }, [props.element]);

    const getSortIcon = (columnKey: string) => {
        if (props.element.getSort()?.fieldId !== columnKey) {
            return '↕️'; // Иконка по умолчанию
        }
        return props.element.getSort()?.direction === 'ASC' ? '↑' : '↓';
    };
    const renderCellContent = (rowId: string, value: any, c: TestColumnDescription) => {
        switch (c.type) {
            case 'TEXT': {
                return value?.toString();
            }
            case 'OPTION': {
                return value?.toString();
            }
            case 'MENU': {
                const menu = value as TestOption[];
                return (
                    <select
                        value="_select"
                        onChange={(ev) => {
                            props.element.processTableAction({
                                rowId,
                                actionId: ev.target.value,
                                columnId: c.id,
                            });
                        }}
                    >
                        <option key="select-action" value="_select">
                            ...
                        </option>
                        {menu.map((m) => (
                            <option key={m.id} value={m.id}>
                                {m.displayName}
                            </option>
                        ))}
                    </select>
                );
            }
            case 'CUSTOM': {
                return value?.toString();
            }
        }
    };
    const modal = useModal();
    return (
        <div
            className="webpeer-container"
            key={props.element.id}
            style={{
                display: 'flex',
                flexDirection: 'column',
            }}
        >
            <div
                className="webpeer-container-header"
                key="header"
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                }}
            >
                <div style={{ flexGrow: 0 }} className="webpeer-tag" key="id">
                    {props.element.tag}
                </div>
                <div style={{ flexGrow: 1 }} key="glue" />
                <button
                    className="webpeer-details-button"
                    style={{ flexGrow: 0 }}
                    onClick={() => {
                        modal.open();
                    }}
                >
                    Details
                </button>
            </div>
            <table className="webpeer-table">
                <thead>
                    <tr>
                        {props.element.getColumns().map((c) => (
                            <th
                                key={c.id}
                                onClick={() => {
                                    if (!c.sortable) {
                                        return;
                                    }
                                    if (props.element.getSort()?.fieldId === c.id) {
                                        const newSort = { ...props.element.getSort()! };
                                        newSort.direction =
                                            newSort.direction == 'ASC' ? 'DESC' : 'ASC';
                                        props.element.setSort(newSort);
                                        props.element.processRefreshData();
                                        return;
                                    }
                                    props.element.setSort({
                                        fieldId: c.id,
                                        direction: 'ASC',
                                    });
                                    props.element.processRefreshData();
                                }}
                            >
                                {c.title} {c.sortable ? getSortIcon(c.id) : ''}
                            </th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {props.element.isLoading() ? (
                        <tr>
                            <td colSpan={props.element.getColumns().length}>Loading</td>
                        </tr>
                    ) : (
                        props.element.getData().map((entry) => (
                            <tr key={entry.id}>
                                {props.element.getColumns().map((c) => (
                                    <td key={c.id}>
                                        {renderCellContent(
                                            entry.id,
                                            (entry as any)[c.id],
                                            c
                                        )}
                                    </td>
                                ))}
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
            <DetailsModal
                element={props.element}
                key="details"
                isOpen={modal.isOpen}
                onClose={modal.close}
            />
        </div>
    );
}

export class TestEntityList extends BaseReactUiElement {
    constructor(model: any) {
        super(
            [],
            ['columns', 'sort', 'data', 'loading'],
            ['refresh-data'],
            ['refresh-data', 'load-more', 'table-action'],
            model
        );
    }

    getColumns() {
        return this.state.get('columns') as EntityListColumnDescription[];
    }

    setSort(sort: TestSort) {
        this.stateSetters.get('sort')!(sort);
        this.sendPropertyChange('sort', sort);
    }
    getSort() {
        return this.state.get('sort') as TestSort | undefined;
    }

    getData() {
        return this.state.get('data') as TestOrganizationEntry[];
    }

    isLoading() {
        return this.state.get('loading') as boolean;
    }

    setLoading(loading: boolean) {
        this.stateSetters.get('loading')?.(loading);
        this.sendPropertyChange('loading', loading);
    }
    processRefreshData() {
        this.setLoading(true);
        this.sendCommand('refresh-data');
    }

    processLoadMore() {
        this.sendCommand('load-more');
    }

    processTableAction(action: TableAction) {
        this.sendCommand('table-action', action);
    }

    processCommandFromServer(commandId: string, data?: any) {
        if ('refresh-data' === commandId) {
            this.processRefreshDataFromServer();
            return;
        }
        super.processCommandFromServer(commandId, data);
    }

    createReactElement(): React.ReactElement {
        return React.createElement(EntityListComponent, {
            element: this,
            key: this.id,
        });
    }

    private processRefreshDataFromServer() {
        this.processRefreshData();
    }
}

export class TestEntityListFactory implements ReactUiElementFactory {
    createElement(model: any): BaseReactUiElement {
        return new TestEntityList(model);
    }
}
