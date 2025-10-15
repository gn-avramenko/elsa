import { MenuProps } from 'antd/lib';
import { Dropdown } from 'antd';

export type ImageMenuItem = {
    id: string;
    imageWidth?: number | string;
    src: string;
    imageHeight?: number | string;
    name: string;
};

export function DropDownImageComp(props: {
    menuItems: ImageMenuItem[];
    style: any;
    selectedId: string;
    callback: (itemId: string) => void;
}) {
    const hs = { ...props.style };

    if (!hs.display) {
        hs.display = 'flex';
        hs.flexDirection = 'row';
        hs.alignItems = 'center';
    }
    hs.verticalAlign = 'center';
    const items: MenuProps['items'] =props.menuItems.map((mi: ImageMenuItem) => ({
        label: (
            <div
                style={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}
            >
                <img
                    style={{ display: 'inline-block' }}
                    alt=""
                    key={mi.id}
                    width={mi.imageWidth}
                    src={mi.src}
                    height={mi.imageHeight}
                />{' '}
                <div style={{ display: 'inline-block', padding: 5 }}>{mi.name}</div>
            </div>
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
                <div style={hs}>
                    <img
                        alt=""
                        key={props.selectedId}
                        width={selectedItem.imageWidth}
                        src={selectedItem.src}
                        height={selectedItem.imageHeight}
                    />{' '}
                </div>
            ) : (
                <span style={hs}>Not Selected</span>
            )}
        </Dropdown>
    );
}
