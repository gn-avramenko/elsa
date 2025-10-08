import { WebComponentWrapper } from '@/common/wrapper';
import { AccountLinkSkeleton } from '@g/app/AccountLinkSkeleton';

function AccountLinkFC(props: { element: AccountLinkComponent }) {
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

export class AccountLinkComponent extends AccountLinkSkeleton {
    functionalComponent = AccountLinkFC;
}
