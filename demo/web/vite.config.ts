import { UserConfig, ConfigEnv } from 'vite';
import { fileURLToPath, URL } from 'url';

// https://vitejs.dev/config/
const defineConfig = ({ mode, command }: ConfigEnv): UserConfig => {
  // @ts-ignore
  const config = {
    server: {
      open: true,
      proxy: {
        '/api': {
          target: 'http://localhost:8080/',
          changeOrigin: false,
        },
      },
    },
    build: {
      rollupOptions: {
        output: {
          entryFileNames: 'assets/[name].[hash].js',
          chunkFileNames: 'assets/[name].[hash].js',
          assetFileNames: 'assets/[name].[hash][extname]',
          manualChunks: (id:string) => {
            if (id.indexOf('/web-core/') !== -1) {
              return 'elsa-core';
            }
            if (id.indexOf('/demo/web/') !== -1) {
              return 'elsa-demo';
            }
            return 'vendor';
          },
        },
        input: {
          app: './index.html', // default
        },
      },
    },
    resolve: {
      alias: [
        {
          find: '@',
          // @ts-ignore
          replacement: fileURLToPath(new URL('./src', import.meta.url)),
        },
      ],
    },
    optimizeDeps: {
      include: ['elsa-core'],
    },
  };
  return config;
};
export default defineConfig;
