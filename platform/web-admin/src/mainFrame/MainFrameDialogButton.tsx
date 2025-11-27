import { initStateSetters } from 'admin/src/common/component';
import { MainFrameDialogButtonSkeleton } from 'admin/src-gen/mainFrame/MainFrameDialogButtonSkeleton';
import { Button } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';

function MainFrameDialogButtonFC(props: { element: MainFrameDialogButtonComponent }) {
    initStateSetters(props.element);
    if (props.element.isHidden()) {
        return <div />;
    }
    return (
        <Button
            disabled={props.element.isDisabled()}
            type={props.element.getButtonType() as any}
            color={props.element.getColor() as any}
            variant="solid"
            icon={props.element.getIcon() && DynamicIcon(props.element.getIcon()!)}
            onClick={() => props.element.sendClick()}
        >
            {props.element.getTitle()}
        </Button>
    );
}

export class MainFrameDialogButtonComponent extends MainFrameDialogButtonSkeleton {
    functionalComponent = MainFrameDialogButtonFC;
}
