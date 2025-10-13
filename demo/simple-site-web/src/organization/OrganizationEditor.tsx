import { WebComponentWrapper } from '@/common/wrapper';
import { initStateSetters } from '@/common/component';
import { OrganizationEditorSkeleton } from '@g/organization/OrganizationEditorSkeleton';
import { EditorWrapper } from '@/common/EditorWraper';

function OrganizationEditorFC(props: { element: OrganizationEditorComponent }) {
    initStateSetters(props.element);
    return (
        <WebComponentWrapper element={props.element}>
            <EditorWrapper element={props.element} />
        </WebComponentWrapper>
    );
}

export class OrganizationEditorComponent extends OrganizationEditorSkeleton {
    functionalComponent = OrganizationEditorFC;
    processResetChanges() {}
}
