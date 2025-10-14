import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { EditorSaveButtonSkeleton } from '@g/common/EditorSaveButtonSkeleton';

function EditorSaveButtonFC(props: { element: EditorSaveButtonComponent }) {
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

export class EditorSaveButtonComponent extends EditorSaveButtonSkeleton {
    functionalComponent = EditorSaveButtonFC;

    setDisabled(value: boolean) {
        this.stateSetters.get('disabled')!(value);
    }
}
