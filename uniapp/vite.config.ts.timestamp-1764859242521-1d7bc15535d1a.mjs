// vite.config.ts
import { defineConfig } from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/vite/dist/node/index.js';
import uni from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/@dcloudio/vite-plugin-uni/dist/index.js';
import { resolve } from 'path';
import Components from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/@uni-helper/vite-plugin-uni-components/dist/index.mjs';
import { WotResolver } from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/@uni-helper/vite-plugin-uni-components/dist/resolvers.mjs';
import eslintPlugin from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/vite-plugin-eslint/dist/index.mjs';
import tailwindcss from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/@tailwindcss/vite/dist/index.mjs';
import TransformPages from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/uni-read-pages-vite/lib/index.js';
import AutoImport from 'file:///E:/%E6%A1%8C%E9%9D%A2/%E8%B6%A3%E4%BC%98/quyouapp/node_modules/unplugin-auto-import/dist/vite.js';
var __vite_injected_original_dirname =
   'E:\\\u684C\u9762\\\u8DA3\u4F18\\quyouapp';
var vite_config_default = defineConfig({
   transpileDependencies: ['z-paging'],
   plugins: [
      tailwindcss(),
      uni.default(),
      Components({
         resolvers: [WotResolver()]
      }),
      AutoImport({
         imports: ['vue', 'uni-app'],
         // 需要自动导入的插件
         dts: 'types/auto-imports.d.ts'
         // 这里是生成的global函数文
      }),
      eslintPlugin({
         include: ['src/**/*.js', 'src/**/*.vue', 'src/**/*.ts'],
         // 指定需要检查的文件
         exclude: ['node_modules/**', 'dist/**'],
         // 指定不需要检查的文件
         fix: false,
         // 是否自动修复
         cache: false
         // 是否启用缓存
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
      sourcemap: false
   },
   resolve: {
      alias: {
         '@': resolve(__vite_injected_original_dirname, 'src')
      }
   },
   server: {
      open: true,
      proxy: {
         '/api': {
            target: 'http://localhost:8080/',
            changeOrigin: true,
            rewrite: path => path.replace(/^\/api/, '')
         }
      }
   }
});
export { vite_config_default as default };
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJFOlxcXFxcdTY4NENcdTk3NjJcXFxcXHU4REEzXHU0RjE4XFxcXHF1eW91YXBwXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJFOlxcXFxcdTY4NENcdTk3NjJcXFxcXHU4REEzXHU0RjE4XFxcXHF1eW91YXBwXFxcXHZpdGUuY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9FOi8lRTYlQTElOEMlRTklOUQlQTIvJUU4JUI2JUEzJUU0JUJDJTk4L3F1eW91YXBwL3ZpdGUuY29uZmlnLnRzXCI7Ly8gdml0ZS5jb25maWcubXRzXHJcbmltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGUnO1xyXG5pbXBvcnQgdW5pIGZyb20gJ0BkY2xvdWRpby92aXRlLXBsdWdpbi11bmknO1xyXG5pbXBvcnQgeyByZXNvbHZlIH0gZnJvbSAncGF0aCc7XHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ0B1bmktaGVscGVyL3ZpdGUtcGx1Z2luLXVuaS1jb21wb25lbnRzJztcclxuaW1wb3J0IHsgV290UmVzb2x2ZXIgfSBmcm9tICdAdW5pLWhlbHBlci92aXRlLXBsdWdpbi11bmktY29tcG9uZW50cy9yZXNvbHZlcnMnO1xyXG5pbXBvcnQgZXNsaW50UGx1Z2luIGZyb20gJ3ZpdGUtcGx1Z2luLWVzbGludCc7XHJcbmltcG9ydCB0YWlsd2luZGNzcyBmcm9tICdAdGFpbHdpbmRjc3Mvdml0ZSc7XHJcbmltcG9ydCBUcmFuc2Zvcm1QYWdlcyBmcm9tICd1bmktcmVhZC1wYWdlcy12aXRlJztcclxuaW1wb3J0IEF1dG9JbXBvcnQgZnJvbSAndW5wbHVnaW4tYXV0by1pbXBvcnQvdml0ZSc7XHJcbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XHJcbiAgIHRyYW5zcGlsZURlcGVuZGVuY2llczogWyd6LXBhZ2luZyddLFxyXG4gICBwbHVnaW5zOiBbXHJcbiAgICAgIHRhaWx3aW5kY3NzKCksXHJcbiAgICAgIHVuaS5kZWZhdWx0KCksXHJcbiAgICAgIENvbXBvbmVudHMoe1xyXG4gICAgICAgICByZXNvbHZlcnM6IFtXb3RSZXNvbHZlcigpXVxyXG4gICAgICB9KSxcclxuICAgICAgQXV0b0ltcG9ydCh7XHJcbiAgICAgICAgIGltcG9ydHM6IFsndnVlJywgJ3VuaS1hcHAnXSwgLy8gXHU5NzAwXHU4OTgxXHU4MUVBXHU1MkE4XHU1QkZDXHU1MTY1XHU3Njg0XHU2M0QyXHU0RUY2XHJcbiAgICAgICAgIGR0czogJ3R5cGVzL2F1dG8taW1wb3J0cy5kLnRzJyAvLyBcdThGRDlcdTkxQ0NcdTY2MkZcdTc1MUZcdTYyMTBcdTc2ODRnbG9iYWxcdTUxRkRcdTY1NzBcdTY1ODdcclxuICAgICAgfSksXHJcblxyXG4gICAgICBlc2xpbnRQbHVnaW4oe1xyXG4gICAgICAgICBpbmNsdWRlOiBbJ3NyYy8qKi8qLmpzJywgJ3NyYy8qKi8qLnZ1ZScsICdzcmMvKiovKi50cyddLCAvLyBcdTYzMDdcdTVCOUFcdTk3MDBcdTg5ODFcdTY4QzBcdTY3RTVcdTc2ODRcdTY1ODdcdTRFRjZcclxuICAgICAgICAgZXhjbHVkZTogWydub2RlX21vZHVsZXMvKionLCAnZGlzdC8qKiddLCAvLyBcdTYzMDdcdTVCOUFcdTRFMERcdTk3MDBcdTg5ODFcdTY4QzBcdTY3RTVcdTc2ODRcdTY1ODdcdTRFRjZcclxuICAgICAgICAgZml4OiBmYWxzZSwgLy8gXHU2NjJGXHU1NDI2XHU4MUVBXHU1MkE4XHU0RkVFXHU1OTBEXHJcbiAgICAgICAgIGNhY2hlOiBmYWxzZSAvLyBcdTY2MkZcdTU0MjZcdTU0MkZcdTc1MjhcdTdGMTNcdTVCNThcclxuICAgICAgfSlcclxuICAgXSxcclxuICAgZGVmaW5lOiB7XHJcbiAgICAgIFJPVVRFUzogbmV3IFRyYW5zZm9ybVBhZ2VzKCkucm91dGVzXHJcbiAgIH0sXHJcbiAgIGNzczoge1xyXG4gICAgICBwcmVwcm9jZXNzb3JPcHRpb25zOiB7XHJcbiAgICAgICAgIHNjc3M6IHtcclxuICAgICAgICAgICAgcXVpZXREZXByZWNhdGlvbnM6IFsnYWxsJ11cclxuICAgICAgICAgfVxyXG4gICAgICB9XHJcbiAgIH0sXHJcbiAgIGJ1aWxkOiB7XHJcbiAgICAgIHNvdXJjZW1hcDogZmFsc2VcclxuICAgfSxcclxuICAgcmVzb2x2ZToge1xyXG4gICAgICBhbGlhczoge1xyXG4gICAgICAgICAnQCc6IHJlc29sdmUoX19kaXJuYW1lLCAnc3JjJylcclxuICAgICAgfVxyXG4gICB9LFxyXG4gICBzZXJ2ZXI6IHtcclxuICAgICAgb3BlbjogdHJ1ZSxcclxuICAgICAgcHJveHk6IHtcclxuICAgICAgICAgJy9hcGknOiB7XHJcbiAgICAgICAgICAgIHRhcmdldDogJ2h0dHA6Ly9sb2NhbGhvc3Q6ODA4MC8nLFxyXG4gICAgICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWUsXHJcbiAgICAgICAgICAgIHJld3JpdGU6IHBhdGggPT4gcGF0aC5yZXBsYWNlKC9eXFwvYXBpLywgJycpXHJcbiAgICAgICAgIH1cclxuICAgICAgfVxyXG4gICB9XHJcbn0pO1xyXG4iXSwKICAibWFwcGluZ3MiOiAiO0FBQ0EsU0FBUyxvQkFBb0I7QUFDN0IsT0FBTyxTQUFTO0FBQ2hCLFNBQVMsZUFBZTtBQUN4QixPQUFPLGdCQUFnQjtBQUN2QixTQUFTLG1CQUFtQjtBQUM1QixPQUFPLGtCQUFrQjtBQUN6QixPQUFPLGlCQUFpQjtBQUN4QixPQUFPLG9CQUFvQjtBQUMzQixPQUFPLGdCQUFnQjtBQVR2QixJQUFNLG1DQUFtQztBQVV6QyxJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUN6Qix1QkFBdUIsQ0FBQyxVQUFVO0FBQUEsRUFDbEMsU0FBUztBQUFBLElBQ04sWUFBWTtBQUFBLElBQ1osSUFBSSxRQUFRO0FBQUEsSUFDWixXQUFXO0FBQUEsTUFDUixXQUFXLENBQUMsWUFBWSxDQUFDO0FBQUEsSUFDNUIsQ0FBQztBQUFBLElBQ0QsV0FBVztBQUFBLE1BQ1IsU0FBUyxDQUFDLE9BQU8sU0FBUztBQUFBO0FBQUEsTUFDMUIsS0FBSztBQUFBO0FBQUEsSUFDUixDQUFDO0FBQUEsSUFFRCxhQUFhO0FBQUEsTUFDVixTQUFTLENBQUMsZUFBZSxnQkFBZ0IsYUFBYTtBQUFBO0FBQUEsTUFDdEQsU0FBUyxDQUFDLG1CQUFtQixTQUFTO0FBQUE7QUFBQSxNQUN0QyxLQUFLO0FBQUE7QUFBQSxNQUNMLE9BQU87QUFBQTtBQUFBLElBQ1YsQ0FBQztBQUFBLEVBQ0o7QUFBQSxFQUNBLFFBQVE7QUFBQSxJQUNMLFFBQVEsSUFBSSxlQUFlLEVBQUU7QUFBQSxFQUNoQztBQUFBLEVBQ0EsS0FBSztBQUFBLElBQ0YscUJBQXFCO0FBQUEsTUFDbEIsTUFBTTtBQUFBLFFBQ0gsbUJBQW1CLENBQUMsS0FBSztBQUFBLE1BQzVCO0FBQUEsSUFDSDtBQUFBLEVBQ0g7QUFBQSxFQUNBLE9BQU87QUFBQSxJQUNKLFdBQVc7QUFBQSxFQUNkO0FBQUEsRUFDQSxTQUFTO0FBQUEsSUFDTixPQUFPO0FBQUEsTUFDSixLQUFLLFFBQVEsa0NBQVcsS0FBSztBQUFBLElBQ2hDO0FBQUEsRUFDSDtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ0wsTUFBTTtBQUFBLElBQ04sT0FBTztBQUFBLE1BQ0osUUFBUTtBQUFBLFFBQ0wsUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLFFBQ2QsU0FBUyxVQUFRLEtBQUssUUFBUSxVQUFVLEVBQUU7QUFBQSxNQUM3QztBQUFBLElBQ0g7QUFBQSxFQUNIO0FBQ0gsQ0FBQzsiLAogICJuYW1lcyI6IFtdCn0K
