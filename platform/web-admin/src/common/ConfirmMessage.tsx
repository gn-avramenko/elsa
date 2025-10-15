import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { ConfirmMessageSkeleton } from '@g/common/ConfirmMessageSkeleton';

function ConfirmMessageFC(props: { element: ConfirmMessageComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            {props.element.getTitle()}
        </WebComponentWrapper>
    );
}
export class ConfirmMessageComponent extends ConfirmMessageSkeleton {
    functionalComponent = ConfirmMessageFC;
}
