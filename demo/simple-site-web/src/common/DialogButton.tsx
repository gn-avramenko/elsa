import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { DialogButtonSkeleton } from '@g/common/DialogButtonSkeleton';

function DialogButtonFC(props: { element: DialogButtonComponent }) {
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

export class DialogButtonComponent extends DialogButtonSkeleton {
    functionalComponent = DialogButtonFC;

    setDisabled(value: boolean) {
        this.stateSetters.get('disabled')!(value);
    }
}
