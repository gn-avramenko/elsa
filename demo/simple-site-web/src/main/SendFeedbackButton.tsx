import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { SendFeedbackButtonSkeleton } from '@g/main/SendFeedbackButtonSkeleton';

function SendFeedbackButtonFC(props: { element: SendFeedbackButtonComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <button
                className="webpeer-button"
                onClick={() => props.element.sendClick()}
            >
                {props.element.getTitle()}
            </button>
        </WebComponentWrapper>
    );
}

export class SendFeedbackButtonComponent extends SendFeedbackButtonSkeleton {
    functionalComponent = SendFeedbackButtonFC;
}
