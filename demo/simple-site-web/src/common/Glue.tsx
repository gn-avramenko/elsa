import { initStateSetters } from '@/common/component';
import { GlueSkeleton } from '@g/common/GlueSkeleton';

function GlueFC(props: { element: GlueComponent }) {
    initStateSetters(props.element);
    return (
        <div
            key={props.element.id}
            style={{
                flexGrow: 1,
            }}
        />
    );
}

export class GlueComponent extends GlueSkeleton {
    functionalComponent = GlueFC;
}
