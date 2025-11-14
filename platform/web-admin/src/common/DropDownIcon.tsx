import { initStateSetters } from 'admin/src/common/component';
import { DropDownIconSkeleton } from 'admin/src-gen/common/DropDownIconSkeleton';
import { MenuProps } from 'antd/lib';
import { DynamicIcon } from 'admin/src/common/extension';
import { Dropdown } from 'antd';

function DropDownIconFC(props: { element: DropDownIconComponent }) {
    initStateSetters(props.element);
    const items: MenuProps['items'] = props.element.getItems().map((mi) => ({
        label: (
            <span>
                {DynamicIcon(mi.icon)} {mi.displayName}
            </span>
        ),
        key: mi.id,
    }));
    return (
        <Dropdown
            placement="bottomLeft"
            menu={{
                items,
                onClick: (item) => {
                    props.element.sendClick({
                        id: item.key,
                    });
                },
            }}
        >
            <div
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center',
                    lineHeight: '15px',
                }}
            >
                <div>
                    {DynamicIcon(props.element.getSelectedItem().icon)}
                    <span>{props.element.getSelectedItem().displayName}</span>
                </div>
            </div>
        </Dropdown>
    );
}

export class DropDownIconComponent extends DropDownIconSkeleton {
    functionalComponent = DropDownIconFC;
}
