import { initStateSetters } from 'admin/src/common/component';
import { MainFrameSkeleton } from 'admin/src-gen/mainFrame/MainFrameSkeleton';
import { Button, ConfigProvider, Drawer, Layout, theme } from 'antd';
import {
    adminWebPeerExt,
    BREAKPOINTS,
    DynamicIcon,
    setWebPeerParam,
} from 'admin/src/common/extension';
import useBreakpoint from 'use-breakpoint';
import { Content, Header } from 'antd/es/layout/layout';
import { PropsWithChildren, useState } from 'react';
import { DropDownImageComp } from 'admin/src/components/dropdown-image-comp';
import { DropDownIconComp } from 'admin/src/components/dropdown-icon-comp';
import useTheme from 'antd/es/config-provider/hooks/useTheme';
import { MenuComp } from 'admin/src/components/menu-comp';
import { MainRouterComponent } from 'admin/src/mainFrame/MainRouter';
import Sider from 'antd/es/layout/Sider';
import { BackwardFilled } from '@ant-design/icons';

function MainFrameFC(props: PropsWithChildren<{ element: MainFrameComponent }>) {
    initStateSetters(props.element);
    const [drawerOpened, setDrawerOpened] = useState(false);
    const darkTheme = !!adminWebPeerExt.useDarkTheme;
    const { token } = theme.useToken();
    let scrollbarColor = darkTheme
        ? 'rgba(255, 255, 255, 0.25) rgba(253, 253, 253, 0.12)'
        : 'rgba(0, 0, 0, 0.25) rgba(6, 6, 6, 0.12)';
    const th = useTheme(
        darkTheme
            ? {
                  algorithm: [theme.darkAlgorithm],
              }
            : {
                  algorithm: [theme.defaultAlgorithm],
                  components: {
                      Layout: {
                          headerColor: 'rgb(255,255,255)',
                          siderBg: 'rgb(255,255,255)',
                          lightTriggerColor: 'rgb(255,255,255)',
                          triggerBg: 'rgb(255,255,255)',
                      },
                  },
              }
    );

    (th as any).token.scrollbarColor = scrollbarColor;
    const lang = adminWebPeerExt.language || 'en';
    const { breakpoint } = useBreakpoint(BREAKPOINTS);
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
                scrollbarColor: scrollbarColor,
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
    const drawHeaderContent = () => (
        <div
            style={{
                width: '100%',
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'center',
                lineHeight: '20px',
                height: '50px',
                padding: token.padding,
                paddingLeft: '5px',
            }}
        >
            <img
                alt=""
                src="/_resources/classpath/admin/logo.svg"
                style={{ display: 'inline-block', height: '45px' }}
            />
            <div
                style={{
                    fontSize: token.fontSizeHeading2,
                    fontWeight: token.fontWeightStrong,
                    padding: token.padding,
                }}
            >
                Admin
            </div>
            <div style={{ flexGrow: 1 }} />
            <div
                style={{
                    fontSize: token.fontSizeHeading3,
                    fontWeight: token.fontWeightStrong,
                    padding: token.padding,
                }}
                dangerouslySetInnerHTML={{ __html: props.element.getTitle() ?? '' }}
            />
            <div style={{ flexGrow: 1 }} />
            <DropDownImageComp
                style={{ padding: token.padding }}
                menuItems={[
                    {
                        src: '/_resources/classpath/admin/en-flag.png',
                        imageHeight: '20px',
                        name: 'English',
                        id: 'en',
                    },
                    {
                        src: '/_resources/classpath/admin/ru-flag.png',
                        imageHeight: '20px',
                        name: 'Russian',
                        id: 'ru',
                    },
                ]}
                selectedId={lang}
                callback={(item) => {
                    setWebPeerParam('lang', item);
                    window.location.reload();
                }}
            />
            <DropDownIconComp
                menuItems={[
                    {
                        name: 'Light',
                        icon: 'SunOutlined',
                        id: 'light',
                    },
                    {
                        name: 'Dark',
                        icon: 'MoonOutlined',
                        id: 'dark',
                    },
                ]}
                style={{ padding: token.padding }}
                selectedId={darkTheme ? 'dark' : 'light'}
                callback={(item) => {
                    setWebPeerParam('useDarkTheme', item === 'dark');
                    window.location.reload();
                }}
            />
        </div>
    );
    const drawMobileHeaderContent = () => (
        <div
            style={{
                width: '100%',
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'center',
                lineHeight: '20px',
                height: '50px',
                padding: token.padding,
                paddingLeft: '5px',
            }}
        >
            <img
                alt=""
                src="/_resources/classpath/admin/logo.svg"
                style={{ display: 'inline-block', height: '45px' }}
            />
            <div
                style={{
                    fontSize: token.fontSizeHeading2,
                    fontWeight: token.fontWeightStrong,
                    padding: token.padding,
                }}
            >
                Admin
            </div>
            <div style={{ flexGrow: 1 }} />
            <DropDownImageComp
                style={{ padding: token.padding }}
                menuItems={[
                    {
                        src: '/_resources/classpath/admin/en-flag.png',
                        imageHeight: '20px',
                        name: 'English',
                        id: 'en',
                    },
                    {
                        src: '/_resources/classpath/admin/ru-flag.png',
                        imageHeight: '20px',
                        name: 'Russian',
                        id: 'ru',
                    },
                ]}
                selectedId={lang}
                callback={(item) => {
                    setWebPeerParam('lang', item);
                    window.location.reload();
                }}
            />
            <DropDownIconComp
                menuItems={[
                    {
                        name: 'Light',
                        icon: 'SunOutlined',
                        id: 'light',
                    },
                    {
                        name: 'Dark',
                        icon: 'MoonOutlined',
                        id: 'dark',
                    },
                ]}
                style={{ padding: token.padding }}
                selectedId={darkTheme ? 'dark' : 'light'}
                callback={(item) => {
                    setWebPeerParam('useDarkTheme', item === 'dark');
                    window.location.reload();
                }}
            />
        </div>
    );
    return (
        <ConfigProvider theme={th}>
            {breakpoint === 'mobile' ? (
                <Layout style={{ height: '100%', borderRadius: token.borderRadiusLG }}>
                    <Header
                        style={{
                            padding: 0,
                            height: 60,
                            display: 'flex',
                            flexDirection: 'row',
                            alignItems: 'center',
                        }}
                    >
                        {props.element.getBackUrl() ? (
                            <div
                                style={{
                                    width: '100%',
                                    display: 'flex',
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    lineHeight: '20px',
                                    height: '50px',
                                    padding: token.padding,
                                    paddingLeft: '5px',
                                }}
                            >
                                <div style={{ flexGrow: 0 }}>
                                    <Button
                                        icon={<BackwardFilled />}
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
                                <div style={{ flexGrow: 1 }} />
                                <div
                                    style={{
                                        fontSize: token.fontSizeHeading3,
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
                            <>
                                <div
                                    style={{
                                        lineHeight: '35px',
                                        padding: token.paddingSM,
                                    }}
                                    onClick={() => {
                                        setDrawerOpened(true);
                                    }}
                                >
                                    {DynamicIcon('MenuFoldOutlined')}
                                </div>
                                {drawMobileHeaderContent()}
                            </>
                        )}
                    </Header>
                    <Content style={{ height: '100%' }}>
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
                            <Content>
                                {props.element
                                    .findByTag('mainRouter')
                                    .createReactElement()}
                            </Content>
                        </Layout>
                    </Content>
                </Layout>
            ) : (
                <Layout style={{ height: '100%', borderRadius: token.borderRadiusLG }}>
                    <Header
                        style={{
                            padding: 0,
                            height: 60,
                            display: 'flex',
                            flexDirection: 'row',
                            alignItems: 'center',
                        }}
                    >
                        {drawHeaderContent()}
                    </Header>
                    <Content style={{ height: '100%' }}>
                        <Layout style={{ height: '100%' }}>
                            <Sider>{drawMenu()}</Sider>
                            <Content>
                                {props.element
                                    .findByTag('mainRouter')
                                    .createReactElement()}
                            </Content>
                        </Layout>
                    </Content>
                </Layout>
            )}
        </ConfigProvider>
    );
}

export class MainFrameComponent extends MainFrameSkeleton {
    functionalComponent = MainFrameFC;
}
