import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { MainFrameDialogFooterSkeleton } from 'admin/src-gen/mainFrame/MainFrameDialogFooterSkeleton';
import { theme } from 'antd';

function MainFrameDialogFooterFC(props: { element: MainFrameDialogFooterComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'flex-end',
            }}
        >
            {(props.element.children || []).map((it) => {
                return (
                    <div key={it.id} style={{ paddingLeft: token.paddingXS }}>
                        {(it as BaseReactUiElement).createReactElement()}
                    </div>
                );
            })}
        </div>
    );
}

export class MainFrameDialogFooterComponent extends MainFrameDialogFooterSkeleton {
    functionalComponent = MainFrameDialogFooterFC;
}
