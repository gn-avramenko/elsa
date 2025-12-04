import { initStateSetters } from 'admin/src/common/component';
import { EditorToolSkeleton } from 'admin/src-gen/entityEditor/EditorToolSkeleton';
import { Button, theme, Tooltip } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';
import { useEditor } from 'admin/src/entityEditor/EntityEditor';

function EditorToolFC(props: { element: EditorToolComponent }) {
    initStateSetters(props.element);
    const editor = useEditor();
    let disabled = false;
    if (props.element.getDisabledByDefault()) {
        disabled = !(props.element.getEnablingTags() || []).find((it) =>
            editor?.hasTag(it)
        );
    } else {
        disabled = !!(props.element.getDisablingTags() || []).find((it) =>
            editor?.hasTag(it)
        );
    }

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
                disabled={!!props.element.getDisabled() || disabled}
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
