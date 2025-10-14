import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { AccountNavigationButtonSkeleton } from '@g/account/AccountNavigationButtonSkeleton';

function AccountNavigationButtonFC(props: {
    element: AccountNavigationButtonComponent;
}) {
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

export class AccountNavigationButtonComponent extends AccountNavigationButtonSkeleton {
    functionalComponent = AccountNavigationButtonFC;
}
