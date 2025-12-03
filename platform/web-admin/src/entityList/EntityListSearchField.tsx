import { initStateSetters } from 'admin/src/common/component';
import { EntityListSearchFieldSkeleton } from 'admin/src-gen/entityList/EntityListSearchFieldSkeleton';
import debounce from 'lodash/debounce';
import { Input } from 'antd';

function EntityListSearchFieldFC(props: { element: EntityListSearchFieldComponent }) {
    initStateSetters(props.element);
    return (
        <Input
            allowClear
            type="text"
            autoComplete="off"
            id="search"
            onChange={(e) => {
                props.element.stateSetters.get('value')!(e.target.value);
                props.element.debouncedSetValue(e.target.value);
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
