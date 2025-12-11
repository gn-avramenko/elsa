import { initStateSetters } from 'admin/src/common/component';
import { RestrictionEditorSkeleton } from 'admin/src-gen/common/RestrictionEditorSkeleton';

function RestrictionEditorFC(props: { element: RestrictionEditorComponent }) {
    initStateSetters(props.element);
    return <div>Hello</div>;
}

export class RestrictionEditorComponent extends RestrictionEditorSkeleton {
    functionalComponent = RestrictionEditorFC;
    redraw() {
        this.parent?.redraw();
    }
}
