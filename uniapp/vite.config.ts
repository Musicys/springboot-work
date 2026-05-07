// vite.config.mts
import { defineConfig } from 'vite';
import uni from '@dcloudio/vite-plugin-uni';
import { resolve } from 'path';
import Components from '@uni-helper/vite-plugin-uni-components';
import { WotResolver } from '@uni-helper/vite-plugin-uni-components/resolvers';
import eslintPlugin from 'vite-plugin-eslint';
import tailwindcss from '@tailwindcss/vite';
import TransformPages from 'uni-read-pages-vite';
import AutoImport from 'unplugin-auto-import/vite';
import { terser } from 'rollup-plugin-terser';
export default defineConfig({
   transpileDependencies: ['z-paging'],
   plugins: [
      tailwindcss(),
      uni.default(),
      Components({
         resolvers: [WotResolver()]
      }),
      AutoImport({
         imports: ['vue', 'uni-app'], // 需要自动导入的插件
         dts: 'types/auto-imports.d.ts' // 这里是生成的global函数文
      }),

      eslintPlugin({
         include: ['src/**/*.js', 'src/**/*.vue', 'src/**/*.ts'], // 指定需要检查的文件
         exclude: ['node_modules/**', 'dist/**'], // 指定不需要检查的文件
         fix: false, // 是否自动修复
         cache: false // 是否启用缓存
      })
   ],
   define: {
      ROUTES: new TransformPages().routes
   },
   css: {
      preprocessorOptions: {
         scss: {
            quietDeprecations: ['all']
         }
      }
   },
   build: {
      minify: 'terser',
      terserOptions: {
         compress: {
            drop_console: true,
            drop_debugger: true
         }
      },
      rollupOptions: {
         plugins: [terser()]
      }
   },
   resolve: {
      alias: {
         '@': resolve(__dirname, 'src')
      }
   },
   server: {
      host: true, // 监听所有地址，包括本机 IP
      port: 5173,
      open: true, // 可选：启动时自动打开浏览器
      proxy: {
         //    '/api': {
         //       target: 'http://localhost:8101/api/',
         //       changeOrigin: true,
         //       rewrite: path => path.replace(/^\/api/, '')
         //    }
      }
   },
   // 启用 terser 压缩（替代默认的 esbuild）
   minify: 'terser',
   terserOptions: {
      compress: {
         // 删除所有 console.*（包括 log, warn, error 等）
         drop_console: true
         // 可选：只删除 console.log
         // pure_funcs: ['console.log']
      },
      // 可选：删除 debugger 语句
      mangle: true
   }
});
