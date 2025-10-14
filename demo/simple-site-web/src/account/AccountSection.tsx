import { WebComponentWrapper } from '@/common/wrapper';
import { BaseReactUiElement, initStateSetters } from '@/common/component';
import { AccountSectionSkeleton } from '@g/account/AccountSectionSkeleton';

function AccountSectionFC(props: { element: AccountSectionComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <div
                className="webpeer-container-content"
                key="content"
                style={{
                    display: 'flex',
                    flexDirection:
                        props.element.getFlexDirection() === 'ROW' ? 'row' : 'column',
                }}
            >
                Account
                {(props.element.children || []).map((it) => {
                    if (props.element.getProcessedChildren().indexOf(it.id) === -1) {
                        return (it as BaseReactUiElement).createReactElement();
                    }
                    return null;
                })}
            </div>
        </WebComponentWrapper>
    );
}

export class AccountSectionComponent extends AccountSectionSkeleton {
    functionalComponent = AccountSectionFC;
    getProcessedChildren(): string[] {
        return [];
    }
}
