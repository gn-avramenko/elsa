import { initStateSetters } from '@/common/component';
import { ButtonSkeleton } from '@g/common/ButtonSkeleton';
import { Button } from 'antd';
import { DynamicIcon } from '@/common/extension';

function ButtonFC(props: { element: ButtonComponent }) {
    initStateSetters(props.element);
    return (
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
