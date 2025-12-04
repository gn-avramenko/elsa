import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { EntityEditorSkeleton } from 'admin/src-gen/entityEditor/EntityEditorSkeleton';
import { theme } from 'antd';

function EntityEditorFC(props: { element: EntityEditorComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'column',
                flexGrow: 1,
                margin: token.paddingXXS,
            }}
        >
            <div
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                    flexGrow: 0,
                    flexShrink: 0,
                }}
            >
                {props.element.findByTag('tools')?.children?.map((ch) => {
                    return (ch as BaseReactUiElement).createReactElement();
                })}
            </div>
            <div
                style={{
                    flexGrow: 1,
                    overflowY: 'auto',
                    marginTop: token.paddingXXS,
                }}
            >
                {props.element.findByTag('content')?.createReactElement()}
            </div>
        </div>
    );
}

export class EntityEditorComponent extends EntityEditorSkeleton {
    functionalComponent = EntityEditorFC;
}
