import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { EditorTitleLabelSkeleton } from '@g/common/EditorTitleLabelSkeleton';

function EditorTitleLabelFC(props: { element: EditorTitleLabelComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            {props.element.getTitle()}
        </WebComponentWrapper>
    );
}
export class EditorTitleLabelComponent extends EditorTitleLabelSkeleton {
    functionalComponent = EditorTitleLabelFC;
    setValidationMessage(value?: string) {
        this.stateSetters.get('validationMessage')!(value);
    }
}
