import { initStateSetters } from 'admin/src/common/component';
import { LabelHeaderSkeleton } from 'admin/src-gen/dialog/LabelHeaderSkeleton';

function LabelHeaderFC(props: { element: LabelHeaderComponent }) {
    initStateSetters(props.element);
    return (
        <div>
            <div className="ant-modal-title">{props.element.getTitle()}</div>
        </div>
    );
}

export class LabelHeaderComponent extends LabelHeaderSkeleton {
    functionalComponent = LabelHeaderFC;
}
