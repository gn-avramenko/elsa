import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { FeedbackContentSkeleton } from '@g/main/FeedbackContentSkeleton';

function FeedbackContentFC(props: { element: FeedbackContentComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <textarea
                value={props.element.getValue()?.value ?? ''}
                className={`webpeer-text-area${props.element.getValidationMessage() ? ' has-error' : ''}`}
                onChange={(e) => {
                    props.element.setValidationMessage(undefined);
                    props.element.setValue({
                        value: e.target.value,
                    });
                }}
            />
        </WebComponentWrapper>
    );
}
export class FeedbackContentComponent extends FeedbackContentSkeleton {
    functionalComponent = FeedbackContentFC;
    setValidationMessage(value?: string) {
        this.stateSetters.get('validationMessage')!(value);
    }
}
