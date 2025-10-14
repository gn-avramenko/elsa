import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { AccountRouterSkeleton } from '@g/account/AccountRouterSkeleton';

function AccountRouterFC(props: { element: AccountRouterComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <div
                className="webpeer-container-content"
                key="content"
                style={{
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                {props.element.findByTag('content').createReactElement()}
            </div>
        </WebComponentWrapper>
    );
}

export class AccountRouterComponent extends AccountRouterSkeleton {
    functionalComponent = AccountRouterFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
