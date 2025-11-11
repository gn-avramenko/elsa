import { webpeerExt } from 'webpeer-core';
import { ReactWebPeerExtension } from 'admin/src/common/component';
import * as AntdIcons from '@ant-design/icons';

export type AdminWebPeerExtension = ReactWebPeerExtension & {
    useDarkTheme?: boolean;
    language?: string;
};
export const adminWebPeerExt = webpeerExt as AdminWebPeerExtension;

export const BREAKPOINTS = { mobile: 0, desktop: 1280 };

export const setWebPeerParam = (key: string, value: any) => {
    let ext2 = JSON.parse(window.localStorage.getItem('webpeer') ?? '{}');
    ext2[key] = value;
    window.localStorage.setItem('webpeer', JSON.stringify(ext2));
};

const ext = JSON.parse(window.localStorage.getItem('webpeer') ?? '{}');
adminWebPeerExt.useDarkTheme = !!ext.useDarkTheme;
adminWebPeerExt.language = ext.lang || 'en';

export const DynamicIcon = (iconName: string) => {
    // @ts-ignore
    const IconComponent = AntdIcons[iconName];

    if (!IconComponent) {
        return null; // Or render a fallback icon
    }

    return <IconComponent />;
};
