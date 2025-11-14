import { initStateSetters } from 'admin/src/common/component';
import { DropDownImageSkeleton } from 'admin/src-gen/common/DropDownImageSkeleton';
import { MenuProps } from 'antd/lib';
import { Dropdown } from 'antd';
import { ImageMenuItem } from 'admin/src-gen/common/ImageMenuItem';

function DropDownImageFC(props: { element: DropDownImageComponent }) {
    initStateSetters(props.element);
    const items: MenuProps['items'] = props.element
        .getItems()
        .map((mi: ImageMenuItem) => ({
            label: (
                <div
                    style={{
                        display: 'flex',
                        flexDirection: 'row',
                        alignItems: 'center',
                    }}
                >
                    <img
                        style={{ display: 'inline-block' }}
                        alt=""
                        key={mi.id}
                        width={mi.imageWidth}
                        src={mi.src}
                        height={mi.imageHeight}
                    />{' '}
                    <div
                        id="displaName"
                        style={{ display: 'inline-block', padding: 5 }}
                    >
                        {mi.displayName}
                    </div>
                </div>
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
                }}
            >
                <img
                    style={{ display: 'inline-block' }}
                    alt=""
                    key={props.element.getSelectedItem().id}
                    width={props.element.getSelectedItem().imageWidth}
                    src={props.element.getSelectedItem().src}
                    height={props.element.getSelectedItem().imageHeight}
                />
                <div style={{ display: 'inline-block' }}>
                    {props.element.getSelectedItem().displayName}
                </div>
            </div>
        </Dropdown>
    );
}

export class DropDownImageComponent extends DropDownImageSkeleton {
    functionalComponent = DropDownImageFC;
}
