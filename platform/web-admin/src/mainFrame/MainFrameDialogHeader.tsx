import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { MainFrameDialogHeaderSkeleton } from 'admin/src-gen/mainFrame/MainFrameDialogHeaderSkeleton';
import { theme } from 'antd';

function MainFrameDialogHeaderFC(props: { element: MainFrameDialogHeaderComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'flex-start',
            }}
        >
            {(props.element.children || []).map((it) => {
                return (
                    <div key={it.id} style={{ paddingRight: token.paddingXS }}>
                        {(it as BaseReactUiElement).createReactElement()}
                    </div>
                );
            })}
        </div>
    );
}

export class MainFrameDialogHeaderComponent extends MainFrameDialogHeaderSkeleton {
    functionalComponent = MainFrameDialogHeaderFC;
}
