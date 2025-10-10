import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { debounce } from '@/common/debounce';
import { SearchFieldSkeleton } from '@g/common/SearchFieldSkeleton';

function SearchFieldFC(props: { element: SearchFieldComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <input
                type="text"
                value={props.element.getValue()?.value ?? ''}
                className={`webpeer-text-field${props.element.getValidationMessage() ? ' has-error' : ''}`}
                onChange={(e) => {
                    props.element.setValidationMessage(undefined);
                    const value = {
                        value: e.target.value,
                    };
                    if (props.element.getDebounceTime()) {
                        props.element.stateSetters.get('value')!(value);
                        props.element.debouncedSetValue(value);
                    } else {
                        props.element.setValue(value);
                    }
                }}
            />
        </WebComponentWrapper>
    );
}
export class SearchFieldComponent extends SearchFieldSkeleton {
    functionalComponent = SearchFieldFC;

    debouncedSetValue = debounce((value: any) => {
        this.sendCommand('pc', {
            pn: 'value',
            pv: value,
        });
    }, this.getDebounceTime() ?? 0);

    setValidationMessage(value?: string) {
        this.stateSetters.get('validationMessage')!(value);
    }
}
