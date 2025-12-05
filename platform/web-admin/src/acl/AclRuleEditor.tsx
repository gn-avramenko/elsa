import { initStateSetters } from 'admin/src/common/component';
import { AclRuleEditorSkeleton } from 'admin/src-gen/acl/AclRuleEditorSkeleton';

function AclRuleEditorFC(props: { element: AclRuleEditorComponent }) {
    initStateSetters(props.element);
    return <div>Rule editor {props.element.id}</div>;
}

export class AclRuleEditorComponent extends AclRuleEditorSkeleton {
    functionalComponent = AclRuleEditorFC;
}
