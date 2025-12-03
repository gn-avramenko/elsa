import { initStateSetters } from 'admin/src/common/component';
import { EditorToolSkeleton } from 'admin/src-gen/entityEditor/EditorToolSkeleton';
import { Button, theme, Tooltip } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';

function EditorToolFC(props: { element: EditorToolComponent }) {
    initStateSetters(props.element);
    if (props.element.getHidden()) {
        return '' as any;
    }
    const { token } = theme.useToken();
    let color = 'primary';
    switch (props.element.getButtonType()) {
        case 'WARNING':
            color = 'yellow';
            break;
        case 'ERROR':
            color = 'danger';
            break;
        case 'SUCCESS':
            color = 'green';
            break;
    }
    return (
        <Tooltip
            title={props.element.getTooltip()}
            placement="topRight"
            mouseEnterDelay={2}
        >
            <Button
                type="primary"
                variant="solid"
                color={color as any}
                disabled={!!props.element.getDisabled()}
                icon={props.element.getIcon() && DynamicIcon(props.element.getIcon()!!)}
                onClick={() => props.element.sendClick()}
                style={{ display: 'inline-block', marginLeft: token.paddingXXS }}
            />
        </Tooltip>
    );
}

export class EditorToolComponent extends EditorToolSkeleton {
    functionalComponent = EditorToolFC;
}
