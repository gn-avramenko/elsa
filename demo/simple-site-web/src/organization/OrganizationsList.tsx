import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { EntityListColumnDescription } from '@g/common/EntityListColumnDescription';
import { Option } from '@g/common/Option';
import { OrganizationsListSkeleton } from '@g/organization/OrganizationsListSkeleton';

function OrganizationsListFC(props: { element: OrganizationsListComponent }) {
    initStateSetters(props.element);
    const getSortIcon = (columnKey: string) => {
        if (props.element.getSort()?.field !== columnKey) {
            return '↕️';
        }
        return props.element.getSort()?.sortOrder === 'ASC' ? '↑' : '↓';
    };
    const renderCellContent = (
        rowId: string,
        value: any,
        c: EntityListColumnDescription
    ) => {
        switch (c.columnType) {
            case 'TEXT': {
                return value?.toString();
            }
            case 'OPTION': {
                return value?.toString();
            }
            case 'MENU': {
                const menu = value as Option[];
                return (
                    <select
                        value="_select"
                        onChange={(ev) => {
                            props.element.sendAction({
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
    return (
        <WebComponentWrapper element={props.element}>
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
                                    if (props.element.getSort()?.field === c.id) {
                                        const newSort = { ...props.element.getSort()! };
                                        newSort.sortOrder =
                                            newSort.sortOrder == 'ASC' ? 'DESC' : 'ASC';
                                        props.element.sendSort({ sort: newSort });
                                        return;
                                    }
                                    props.element.sendSort({
                                        sort: {
                                            field: c.id,
                                            sortOrder: 'ASC',
                                        },
                                    });
                                }}
                            >
                                {c.title} {c.sortable ? getSortIcon(c.id) : ''}
                            </th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {props.element.getLoading() ? (
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
        </WebComponentWrapper>
    );
}
export class OrganizationsListComponent extends OrganizationsListSkeleton {
    functionalComponent = OrganizationsListFC;

    processRefreshData() {
        this.sendRefreshData();
    }
}
