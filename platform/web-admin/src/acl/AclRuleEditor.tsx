import { initStateSetters } from 'admin/src/common/component';
import { AclRuleEditorSkeleton } from 'admin/src-gen/acl/AclRuleEditorSkeleton';
import { PropsWithChildren } from 'react';
import { theme } from 'antd';

function AclBlock(props: PropsWithChildren<{ title: string }>) {
    const { token } = theme.useToken();
    return (
        <div
            style={{
                borderStyle: 'solid',
                borderWidth: 1,
                borderColor: token.colorBorder,
                borderRadius: token.borderRadius,
                marginTop: token.marginSM,
                position: 'relative',
            }}
        >
            <div
                style={{
                    position: 'absolute',
                    backgroundColor: token.colorBgBase,
                    color: token.colorTextDisabled,
                    top: -10,
                    left: 10,
                }}
            >
                {props.title}
            </div>
            <div style={{ padding: token.paddingXS, paddingTop: 13 }}>
                {props.children}
            </div>
        </div>
    );
}

function AclRuleEditorFC(props: { element: AclRuleEditorComponent }) {
    initStateSetters(props.element);
    return (
        <>
            <AclBlock title={props.element.getConditionsTitle()} />
            <AclBlock title={props.element.getActionsTitle()}>
                {props.element.findByTag('actions').createReactElement()}
            </AclBlock>
        </>
    );
}

export class AclRuleEditorComponent extends AclRuleEditorSkeleton {
    functionalComponent = AclRuleEditorFC;
}
