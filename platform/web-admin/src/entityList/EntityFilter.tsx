import { initStateSetters } from '@/common/component';
import { EntityFilterSkeleton } from '@g/entityList/EntityFilterSkeleton';
import { DebounceSelect } from '@/components/DebounceSelect';

function EntityFilterFC(props: { element: EntityFilterComponent }) {
    initStateSetters(props.element);
    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'flex-start',
                padding: 5,
            }}
        >
            <div>{props.element.getTitle()}</div>
            <DebounceSelect
                mode="multiple"
                size="small"
                allowClear
                debounceTimeout={props.element.getDebounceTime() ?? 300}
                value={
                    props.element.getValue()?.values?.map((it) => ({
                        key: it.id,
                        label: it.displayName,
                        value: it.id,
                    })) || []
                }
                fetchOptions={async (query) => {
                    const response = await props.element.doGetData({
                        query,
                        limit: props.element.getLimit(),
                    });
                    return (response.items || []).map((it) => ({
                        key: it.id,
                        label: it.displayName,
                        value: it.id,
                    }));
                }}
                style={{ width: '100%', paddingTop: 5 }}
                onChange={(newValue) => {
                    if (Array.isArray(newValue)) {
                        props.element.setValue({
                            values: (newValue as any[]).map((it) => ({
                                id: it.value,
                                displayName: it.label,
                            })),
                        });
                    }
                }}
            />
        </div>
    );
}

export class EntityFilterComponent extends EntityFilterSkeleton {
    functionalComponent = EntityFilterFC;
}
