import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { debounce } from '@/common/debounce';
import { useEditor } from "@/common/editor";
import { StandardTextFieldSkeleton } from '@g/common/StandardTextFieldSkeleton';

function StandardTextFieldFC(props: { element: StandardTextFieldComponent }) {
    initStateSetters(props.element);
    const editor = useEditor();
    return (
        <WebComponentWrapper element={props.element}>
            <input
               type="text"
               value={props.element.getValue()?.value??''}
               className={`webpeer-text-field${props.element.getValidationMessage() ? ' has-error' : ''}`}
               onChange={(e) => {
                        props.element.setValidationMessage(undefined);
                        if(editor) {
                           editor.setHasChanges(true);
                        }
                        const value = {
                          value: e.target.value
                        };
                        if(props.element.getDebounceTime()){
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
export class StandardTextFieldComponent extends StandardTextFieldSkeleton {
    functionalComponent = StandardTextFieldFC;

    debouncedSetValue = debounce((value: any) => {
           this.sendCommand(
                'pc',
                {
                    pn: 'value',
                    pv: value,
                });
        }, this.getDebounceTime()??0);

    setValidationMessage(value?: string) {
        this.stateSetters.get('validationMessage')!(value);
    }
}
