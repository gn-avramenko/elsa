import { initStateSetters } from 'admin/src/common/component';
import { GlueSkeleton } from 'admin/src-gen/common/GlueSkeleton';

function GlueFC(props: { element: GlueComponent }) {
    initStateSetters(props.element);
    return <div style={{ flexGrow: 1 }} />;
}

export class GlueComponent extends GlueSkeleton {
    functionalComponent = GlueFC;
}
