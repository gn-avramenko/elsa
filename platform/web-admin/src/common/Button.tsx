import { initStateSetters } from 'admin/src/common/component';
import { ButtonSkeleton } from 'admin/src-gen/common/ButtonSkeleton';
import { Button, Tooltip } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';

function ButtonFC(props: { element: ButtonComponent }) {
    initStateSetters(props.element);
    return props.element.getTooltip() ? (
        <Tooltip mouseEnterDelay={1} title={props.element.getTooltip()}>
            <Button
                type="primary"
                disabled={!!props.element.getDisabled()}
                icon={props.element.getIcon() && DynamicIcon(props.element.getIcon()!!)}
                onClick={() => props.element.sendClick()}
            >
                {props.element.getTitle()}
            </Button>
        </Tooltip>
    ) : (
        <Button
            type="primary"
            disabled={!!props.element.getDisabled()}
            icon={props.element.getIcon() && DynamicIcon(props.element.getIcon()!!)}
            onClick={() => props.element.sendClick()}
        >
            {props.element.getTitle()}
        </Button>
    );
}

export class ButtonComponent extends ButtonSkeleton {
    functionalComponent = ButtonFC;
}
