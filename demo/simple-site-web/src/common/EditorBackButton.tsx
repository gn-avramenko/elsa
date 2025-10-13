import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { EditorBackButtonSkeleton } from '@g/common/EditorBackButtonSkeleton';

function EditorBackButtonFC(props: { element: EditorBackButtonComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
             <button
             disabled={!!props.element.getDisabled()}
                                  className="webpeer-button"
                                  onClick={() => props.element.sendClick()}
                              >
                                  {props.element.getTitle()}
                              </button>
        </WebComponentWrapper>
    );
}

export class EditorBackButtonComponent extends EditorBackButtonSkeleton {
    functionalComponent = EditorBackButtonFC;

    setDisabled(value: boolean){
       this.stateSetters.get('disabled')!(value)
    }
}
