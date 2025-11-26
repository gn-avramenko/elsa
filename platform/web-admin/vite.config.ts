import { UserConfig, ConfigEnv } from 'vite';
import { resolve } from 'path';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
const defineConfig = ({ mode, command }: ConfigEnv): UserConfig => {
    // @ts-ignore
    const config = {
        plugins: [
            react({
                babel: {
                    plugins: [
                        [
                            'babel-plugin-styled-components',
                            {
                                displayName: mode === 'development',
                                fileName: false,
                            },
                        ],
                    ],
                },
            }),
        ],
        server: {
            open: true,
            proxy: {
                '/_ui': {
                    target: 'http://localhost:8080/',
                    changeOrigin: false,
                },
                '/_resources': {
                    target: 'http://localhost:8080/',
                    changeOrigin: false,
                },
                '/websocket': {
                    // Proxy requests starting with /websocket
                    target: 'http://localhost:8080/', // Target backend server for WebSocket
                    ws: true, // Enable WebSocket proxying
                    changeOrigin: true, // Change the origin header to the target URL
                },
            },
        },
        build: {
            sourcemap: true,
            target: 'esnext',
            rollupOptions: {
                input: {
                    app: './index.html', // default
                },
            },
        },
        resolve: {
            alias: {
                'webpeer-core': resolve(
                    __dirname,
                    '../../submodules/webpeer/web/core/src/index'
                ), // Базовый алиас для папки src
                admin: __dirname,
            },
        },
        css: {
            preprocessorOptions: {
                scss: {
                    silenceDeprecations: [
                        'import',
                        'mixed-decls',
                        'color-functions',
                        'global-builtin',
                    ],
                },
            },
        },
    };
    return config;
};
export default defineConfig;
