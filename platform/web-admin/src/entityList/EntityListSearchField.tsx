import { initStateSetters } from '@/common/component';
import { debounce } from '@/common/debounce';
import { EntityListSearchFieldSkeleton } from '@g/entityList/EntityListSearchFieldSkeleton';
import Search from 'antd/es/input/Search';

function EntityListSearchFieldFC(props: { element: EntityListSearchFieldComponent }) {
    initStateSetters(props.element);
    return (
        <Search
            allowClear
            id="search"
            onSearch={(text) => {
                const value = {
                    value: text,
                };
                props.element.stateSetters.get('value')!(value);
                props.element.setValue(value);
            }}
            onChange={(e) => {
                const value = {
                    value: e.target.value,
                };
                props.element.stateSetters.get('value')!(value);
                props.element.debouncedSetValue(value);
            }}
        />
    );
}
export class EntityListSearchFieldComponent extends EntityListSearchFieldSkeleton {
    functionalComponent = EntityListSearchFieldFC;

    debouncedSetValue = debounce((value: any) => {
        this.sendCommand('pc', {
            pn: 'value',
            pv: value,
        });
    }, this.getDebounceTime() ?? 0);
}
