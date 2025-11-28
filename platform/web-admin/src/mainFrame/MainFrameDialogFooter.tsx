import { BaseReactUiElement, initStateSetters } from 'admin/src/common/component';
import { MainFrameDialogFooterSkeleton } from 'admin/src-gen/mainFrame/MainFrameDialogFooterSkeleton';
import { Flex, theme } from 'antd';

function MainFrameDialogFooterFC(props: { element: MainFrameDialogFooterComponent }) {
    initStateSetters(props.element);
    const { token } = theme.useToken();
    return (
        <Flex justify="flex-end" wrap gap="small">
            {(props.element.children || []).map((it) => {
                return (
                    <div key={it.id} style={{ paddingLeft: token.paddingXS }}>
                        {(it as BaseReactUiElement).createReactElement()}
                    </div>
                );
            })}
        </Flex>
    );
}

export class MainFrameDialogFooterComponent extends MainFrameDialogFooterSkeleton {
    functionalComponent = MainFrameDialogFooterFC;
}
