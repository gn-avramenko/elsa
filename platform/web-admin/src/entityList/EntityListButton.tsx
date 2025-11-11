import { initStateSetters } from 'admin/src/common/component';
import { EntityListButtonSkeleton } from 'admin/src-gen/entityList/EntityListButtonSkeleton';
import { Button, Tooltip } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';

function EntityListButtonFC(props: { element: EntityListButtonComponent }) {
    initStateSetters(props.element);
    return (
        <Tooltip
            title={props.element.getTitle()}
            placement="topRight"
            mouseEnterDelay={2}
        >
            <Button
                type="primary"
                disabled={!!props.element.getDisabled()}
                icon={props.element.getIcon() && DynamicIcon(props.element.getIcon()!!)}
                onClick={() => props.element.sendClick()}
            />
        </Tooltip>
    );
}

export class EntityListButtonComponent extends EntityListButtonSkeleton {
    functionalComponent = EntityListButtonFC;
}
