import { WebComponentWrapper } from '@/common/wrapper';
import { StandardLinkSkeleton } from '@g/app/StandardLinkSkeleton';

function StandardLinkFC(props: { element: StandardLinkComponent }) {
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

export class StandardLinkComponent extends StandardLinkSkeleton {
    functionalComponent = StandardLinkFC;
}
