import { initStateSetters } from 'admin/src/common/component';
import { MainFrameDialogButtonSkeleton } from 'admin/src-gen/mainFrame/MainFrameDialogButtonSkeleton';
import { Button } from 'antd';

function MainFrameDialogButtonFC(props: { element: MainFrameDialogButtonComponent }) {
    initStateSetters(props.element);
    return (
        <Button onClick={() => props.element.sendClick()}>
            {props.element.getTitle()}
        </Button>
    );
}

export class MainFrameDialogButtonComponent extends MainFrameDialogButtonSkeleton {
    functionalComponent = MainFrameDialogButtonFC;
}
