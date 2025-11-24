import {
    BaseReactUiElement,
    initStateSetters,
    preloaderHolder,
} from 'admin/src/common/component';
import { MainFrameSkeleton } from 'admin/src-gen/mainFrame/MainFrameSkeleton';
import { ConfigProvider, Drawer, Layout, Modal, Spin, theme } from 'antd';
import { BREAKPOINTS, DynamicIcon, setWebPeerParam } from 'admin/src/common/extension';
import useBreakpoint from 'use-breakpoint';
import { Content, Header } from 'antd/es/layout/layout';
import { createContext, PropsWithChildren, useContext, useState } from 'react';
import useTheme from 'antd/es/config-provider/hooks/useTheme';
import { MenuComp } from 'admin/src/components/menu-comp';
import { MainRouterComponent } from 'admin/src/mainFrame/MainRouter';
import Sider from 'antd/es/layout/Sider';
import { ArrowLeftOutlined, ExclamationCircleFilled } from '@ant-design/icons';
import { MainFrameSetWebPeerParamAction } from 'admin/src-gen/mainFrame/MainFrameSetWebPeerParamAction';
import { MainFrameSaveFileAction } from 'admin/src-gen/mainFrame/MainFrameSaveFileAction';
import { MainFrameShowConfirmationDialogAction } from 'admin/src-gen/mainFrame/MainFrameShowConfirmationDialogAction';
import { MainFrameShowErrorAction } from 'admin/src-gen/mainFrame/MainFrameShowErrorAction';

const ExtThemePropertiesContext = createContext<any>({});

export const useExtThemeProperties = (): any => {
    return useContext(ExtThemePropertiesContext);
};

function MainFrameFC(props: PropsWithChildren<{ element: MainFrameComponent }>) {
    initStateSetters(props.element);
    const [drawerOpened, setDrawerOpened] = useState(false);
    const [spinning, setSpinning] = useState(false);
    preloaderHolder.hidePreloader = () => setSpinning(false);
    preloaderHolder.showPreloader = () => setSpinning(true);
    const { token } = theme.useToken();
    const themeConfig = JSON.parse(props.element.getThemeToken()) as any;
    if (themeConfig.algorithm) {
        themeConfig.algorithm = themeConfig.algorithm.map(
            (it: any) => (theme as any)[it]
        );
    }
    const ext = themeConfig.ext;
    const th = useTheme(themeConfig);
    const { breakpoint } = useBreakpoint(BREAKPOINTS);
    if (props.element.getEmbeddedMode()) {
        return (
            <ExtThemePropertiesContext.Provider value={ext}>
                <ConfigProvider theme={th}>
                    <Layout style={{ height: '100%' }}>
                        <Content>
                            {props.element.findByTag('mainRouter').createReactElement()}
                        </Content>
                    </Layout>
                </ConfigProvider>
            </ExtThemePropertiesContext.Provider>
        );
    }
    //customize token
    const drawMenu = () => (
        <MenuComp
            menuGroups={props.element.getWorkspaceGroups().map((mg, idx) => {
                return {
                    icon: mg.icon,
                    name: mg.name,
                    id: `group-${idx}`,
                    children: mg.items.map((mi) => {
                        return {
                            icon: mi.icon,
                            name: mi.name,
                            id: mi.link,
                        };
                    }),
                };
            })}
            style={{
                height: '100%',
                overflowY: 'auto',
                scrollbarColor: ext.scrollbarColor,
            }}
            callback={(link) => {
                setDrawerOpened(false);
                (props.element.findByTag('mainRouter') as MainRouterComponent).navigate(
                    {
                        path: link,
                        force: false,
                    }
                );
            }}
        />
    );
    return (
        <ConfigProvider theme={th}>
            {breakpoint === 'mobile' ? (
                <Layout style={{ height: '100%', borderRadius: token.borderRadiusLG }}>
                    <Header
                        style={{
                            padding: 0,
                            display: 'flex',
                            flexDirection: 'row',
                            alignItems: 'center',
                        }}
                    >
                        {props.element.getBackUrl() ? (
                            <div
                                key="header"
                                style={{
                                    width: '100%',
                                    display: 'flex',
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    padding: token.padding,
                                    paddingLeft: '5px',
                                }}
                            >
                                <div style={{ flexGrow: 0 }} key="back">
                                    <ArrowLeftOutlined
                                        onClick={() => {
                                            (
                                                props.element.findByTag(
                                                    'mainRouter'
                                                ) as MainRouterComponent
                                            ).navigate({
                                                path: props.element.getBackUrl()!!,
                                                force: false,
                                            });
                                        }}
                                    />
                                </div>
                                <div key="glue-1" style={{ flexGrow: 1 }} />
                                <div
                                    key="title"
                                    style={{
                                        fontSize: token.fontSizeHeading5,
                                        fontWeight: token.fontWeightStrong,
                                        padding: token.padding,
                                    }}
                                    dangerouslySetInnerHTML={{
                                        __html: props.element.getTitle() ?? '',
                                    }}
                                />
                                <div style={{ flexGrow: 1 }} />
                            </div>
                        ) : (
                            <div
                                style={{
                                    width: '100%',
                                    display: 'flex',
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: '5px',
                                }}
                            >
                                <div
                                    key="menu"
                                    onClick={() => {
                                        setDrawerOpened(true);
                                    }}
                                >
                                    {DynamicIcon('MenuFoldOutlined')}
                                </div>
                                <div
                                    key="title"
                                    style={{
                                        fontSize: token.fontSizeHeading2,
                                        fontWeight: token.fontWeightStrong,
                                        paddingLeft: token.paddingXS,
                                        paddingBottom: '3px',
                                    }}
                                >
                                    {props.element.getAppName()}
                                </div>
                                <div style={{ flexGrow: 1 }} />
                                <div
                                    key="tools"
                                    style={{
                                        display: 'flex',
                                        flexDirection: 'row',
                                        alignItems: 'center',
                                    }}
                                >
                                    {(
                                        props.element.findByTag('header-tools')
                                            ?.children || []
                                    ).map((it) => (
                                        <div
                                            key={`tool-wrapper-${it.id}`}
                                            style={{
                                                padding: token.paddingXS,
                                            }}
                                        >
                                            {(
                                                it as BaseReactUiElement
                                            ).createReactElement()}
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </Header>
                    <Content style={{ height: '100%' }}>
                        <Spin spinning={spinning} fullscreen={true} />
                        <Drawer
                            styles={{
                                header: { padding: 5 },
                                content: { padding: 0 },
                                body: { padding: 5 },
                            }}
                            closable
                            onClose={() => setDrawerOpened(false)}
                            open={drawerOpened}
                        >
                            {drawMenu()}
                        </Drawer>
                        <Layout style={{ height: '100%' }}>
                            <ExtThemePropertiesContext.Provider value={ext}>
                                <Content>
                                    {props.element
                                        .findByTag('mainRouter')
                                        .createReactElement()}
                                </Content>
                            </ExtThemePropertiesContext.Provider>
                        </Layout>
                    </Content>
                </Layout>
            ) : (
                <Layout style={{ height: '100%', borderRadius: token.borderRadiusLG }}>
                    <Sider>
                        <Header
                            style={{
                                padding: 0,
                                display: 'flex',
                                flexDirection: 'row',
                                alignItems: 'center',
                            }}
                        >
                            <div
                                style={{
                                    fontSize: token.fontSizeHeading4,
                                    fontWeight: token.fontWeightStrong,
                                    paddingLeft: token.padding,
                                    lineHeight: token.lineHeightHeading4,
                                }}
                            >
                                {props.element.getAppName()}
                            </div>
                        </Header>
                        <Content>{drawMenu()}</Content>
                    </Sider>
                    <Content
                        style={{
                            height: '100%',
                            display: 'flex',
                            flexDirection: 'column',
                        }}
                    >
                        <Header
                            style={{
                                flexGrow: 0,
                                padding: 0,
                                display: 'flex',
                                flexDirection: 'row',
                                alignItems: 'center',
                            }}
                        >
                            <div key="glue-1" style={{ flexGrow: 1 }} />
                            <div
                                key="title"
                                style={{
                                    fontSize: token.fontSizeHeading4,
                                    fontWeight: token.fontWeightStrong,
                                    padding: token.paddingXS,
                                    lineHeight: token.lineHeightHeading4,
                                }}
                                dangerouslySetInnerHTML={{
                                    __html: props.element.getTitle() ?? '',
                                }}
                            />
                            <div key="glue-2" style={{ flexGrow: 1 }} />
                            <div
                                key="tools"
                                style={{
                                    display: 'flex',
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                }}
                            >
                                {(
                                    props.element.findByTag('header-tools')?.children ||
                                    []
                                ).map((it) => (
                                    <div
                                        key={`tool-wrapper-${it.id}`}
                                        style={{
                                            padding: token.paddingXS,
                                        }}
                                    >
                                        {(
                                            it as BaseReactUiElement
                                        ).createReactElement()}
                                    </div>
                                ))}
                            </div>
                        </Header>
                        <Content style={{ flexGrow: 1 }}>
                            <Spin spinning={spinning} fullscreen={true} />
                            <ExtThemePropertiesContext.Provider value={ext}>
                                {props.element
                                    .findByTag('mainRouter')
                                    .createReactElement()}
                            </ExtThemePropertiesContext.Provider>
                        </Content>
                    </Content>
                </Layout>
            )}
        </ConfigProvider>
    );
}

export class MainFrameComponent extends MainFrameSkeleton {
    processShowError(value: MainFrameShowErrorAction): void {
        Modal.error({
            title: value.title,
            okText: value.closeText,
            content: value.error,
        });
    }
    processShowConfirmationDialog(value: MainFrameShowConfirmationDialogAction): void {
        Modal.confirm({
            icon: <ExclamationCircleFilled />,
            title: value.title,
            okText: value.okText,
            cancelText: value.cancelText,
            content: value.question,
            onOk: () => {
                this.sendProcessConfirmationResult({
                    okPressed: true,
                });
            },
            onCancel: () => {
                this.sendProcessConfirmationResult({
                    okPressed: false,
                });
            },
        });
    }
    processSaveFile(value: MainFrameSaveFileAction): void {
        const link = document.createElement('a');
        link.download = value.name;
        link.href = `data:application/octet-stream;base64,${value.base64Content}`;
        link.className = 'external';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
    processReloadPage(): void {
        window.location.reload();
    }
    processSetWebPeerParam(value: MainFrameSetWebPeerParamAction): void {
        setWebPeerParam(value.key, value.value);
        window.location.reload();
    }
    functionalComponent = MainFrameFC;
}
