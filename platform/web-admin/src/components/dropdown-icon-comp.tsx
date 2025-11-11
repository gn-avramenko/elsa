import { MenuProps } from 'antd/lib';
import { Dropdown } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';

export type IconMenuItem = {
    id: string;
    icon: string;
    name: string;
};

export function DropDownIconComp(props: {
    menuItems: IconMenuItem[];
    style: any;
    selectedId: string;
    callback: (itemId: string) => void;
}) {
    const items: MenuProps['items'] = props.menuItems.map((mi: IconMenuItem) => ({
        label: (
            <span>
                {DynamicIcon(mi.icon)} {mi.name}
            </span>
        ),
        key: mi.id,
    }));
    const selectedItem = props.menuItems.find((it) => it.id === props.selectedId);
    return (
        <Dropdown
            placement="bottomLeft"
            menu={{
                items,
                onClick: (item) => {
                    props.callback(item.key);
                },
            }}
        >
            {selectedItem ? (
                <div style={props.style}>{DynamicIcon(selectedItem.icon)}</div>
            ) : (
                <span>Not selected</span>
            )}
        </Dropdown>
    );
}
