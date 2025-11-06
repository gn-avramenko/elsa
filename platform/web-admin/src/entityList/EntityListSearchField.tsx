import { initStateSetters } from '@/common/component';
import { EntityListSearchFieldSkeleton } from '@g/entityList/EntityListSearchFieldSkeleton';
import Search from 'antd/es/input/Search';
import debounce from 'lodash/debounce';

function EntityListSearchFieldFC(props: { element: EntityListSearchFieldComponent }) {
    initStateSetters(props.element);
    return (
        <Search
            allowClear
            id="search"
            onSearch={(text) => {
                props.element.stateSetters.get('value')!(text);
                props.element.setValue(text);
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
