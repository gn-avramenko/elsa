import { MenuProps } from 'antd/lib';
import { Menu } from 'antd';
import { DynamicIcon } from 'admin/src/common/extension';

export type MenuItem = {
    id: string;
    icon?: string;
    name: string;
};

export type MenuGroup = {
    id: string;
    icon?: string;
    name: string;
    children: MenuItem[];
};

export function MenuComp(props: {
    menuGroups: MenuGroup[];
    style: any;
    callback: (itemId: string) => void;
}) {
    const items: MenuProps['items'] = props.menuGroups.map((mg: MenuGroup) => ({
        label: (
            <span>
                {mg.icon && DynamicIcon(mg.icon)} {mg.name}
            </span>
        ),
        key: mg.id,
        children: mg.children.map((mi) => ({
            key: mi.id,
            label: (
                <span>
                    {mi.icon && DynamicIcon(mi.icon)} {mi.name}
                </span>
            ),
            onClick: () => {
                props.callback(mi.id);
            },
        })),
    }));
    return (
        <Menu
            mode="inline"
            defaultSelectedKeys={['']}
            defaultOpenKeys={[props.menuGroups[0].id]}
            style={props.style}
            items={items}
        />
    );
}
