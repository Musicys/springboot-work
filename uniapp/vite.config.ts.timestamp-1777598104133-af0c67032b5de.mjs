// vite.config.ts
import { defineConfig } from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/vite/dist/node/index.js";
import uni from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/@dcloudio/vite-plugin-uni/dist/index.js";
import { resolve } from "path";
import Components from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/@uni-helper/vite-plugin-uni-components/dist/index.mjs";
import { WotResolver } from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/@uni-helper/vite-plugin-uni-components/dist/resolvers.mjs";
import eslintPlugin from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/vite-plugin-eslint/dist/index.mjs";
import tailwindcss from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/@tailwindcss/vite/dist/index.mjs";
import TransformPages from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/uni-read-pages-vite/lib/index.js";
import AutoImport from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/unplugin-auto-import/dist/vite.js";
import { terser } from "file:///E:/%E6%A1%8C%E9%9D%A2/%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87/xm/uniapp/node_modules/rollup-plugin-terser/rollup-plugin-terser.mjs";
var __vite_injected_original_dirname = "E:\\\u684C\u9762\\\u6BD5\u4E1A\u8BBA\u6587\\xm\\uniapp";
var vite_config_default = defineConfig({
  transpileDependencies: ["z-paging"],
  plugins: [
    tailwindcss(),
    uni.default(),
    Components({
      resolvers: [WotResolver()]
    }),
    AutoImport({
      imports: ["vue", "uni-app"],
      // 需要自动导入的插件
      dts: "types/auto-imports.d.ts"
      // 这里是生成的global函数文
    }),
    eslintPlugin({
      include: ["src/**/*.js", "src/**/*.vue", "src/**/*.ts"],
      // 指定需要检查的文件
      exclude: ["node_modules/**", "dist/**"],
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
        quietDeprecations: ["all"]
      }
    }
  },
  build: {
    minify: "terser",
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
      "@": resolve(__vite_injected_original_dirname, "src")
    }
  },
  server: {
    host: true,
    // 监听所有地址，包括本机 IP
    port: 5173,
    open: true,
    // 可选：启动时自动打开浏览器
    proxy: {
      "/api": {
        target: "http://localhost:8080/",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  },
  // 启用 terser 压缩（替代默认的 esbuild）
  minify: "terser",
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
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJFOlxcXFxcdTY4NENcdTk3NjJcXFxcXHU2QkQ1XHU0RTFBXHU4QkJBXHU2NTg3XFxcXHhtXFxcXHVuaWFwcFwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRTpcXFxcXHU2ODRDXHU5NzYyXFxcXFx1NkJENVx1NEUxQVx1OEJCQVx1NjU4N1xcXFx4bVxcXFx1bmlhcHBcXFxcdml0ZS5jb25maWcudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0U6LyVFNiVBMSU4QyVFOSU5RCVBMi8lRTYlQUYlOTUlRTQlQjglOUElRTglQUUlQkElRTYlOTYlODcveG0vdW5pYXBwL3ZpdGUuY29uZmlnLnRzXCI7Ly8gdml0ZS5jb25maWcubXRzXHJcbmltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGUnO1xyXG5pbXBvcnQgdW5pIGZyb20gJ0BkY2xvdWRpby92aXRlLXBsdWdpbi11bmknO1xyXG5pbXBvcnQgeyByZXNvbHZlIH0gZnJvbSAncGF0aCc7XHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ0B1bmktaGVscGVyL3ZpdGUtcGx1Z2luLXVuaS1jb21wb25lbnRzJztcclxuaW1wb3J0IHsgV290UmVzb2x2ZXIgfSBmcm9tICdAdW5pLWhlbHBlci92aXRlLXBsdWdpbi11bmktY29tcG9uZW50cy9yZXNvbHZlcnMnO1xyXG5pbXBvcnQgZXNsaW50UGx1Z2luIGZyb20gJ3ZpdGUtcGx1Z2luLWVzbGludCc7XHJcbmltcG9ydCB0YWlsd2luZGNzcyBmcm9tICdAdGFpbHdpbmRjc3Mvdml0ZSc7XHJcbmltcG9ydCBUcmFuc2Zvcm1QYWdlcyBmcm9tICd1bmktcmVhZC1wYWdlcy12aXRlJztcclxuaW1wb3J0IEF1dG9JbXBvcnQgZnJvbSAndW5wbHVnaW4tYXV0by1pbXBvcnQvdml0ZSc7XHJcbmltcG9ydCB7IHRlcnNlciB9IGZyb20gJ3JvbGx1cC1wbHVnaW4tdGVyc2VyJztcclxuZXhwb3J0IGRlZmF1bHQgZGVmaW5lQ29uZmlnKHtcclxuICAgdHJhbnNwaWxlRGVwZW5kZW5jaWVzOiBbJ3otcGFnaW5nJ10sXHJcbiAgIHBsdWdpbnM6IFtcclxuICAgICAgdGFpbHdpbmRjc3MoKSxcclxuICAgICAgdW5pLmRlZmF1bHQoKSxcclxuICAgICAgQ29tcG9uZW50cyh7XHJcbiAgICAgICAgIHJlc29sdmVyczogW1dvdFJlc29sdmVyKCldXHJcbiAgICAgIH0pLFxyXG4gICAgICBBdXRvSW1wb3J0KHtcclxuICAgICAgICAgaW1wb3J0czogWyd2dWUnLCAndW5pLWFwcCddLCAvLyBcdTk3MDBcdTg5ODFcdTgxRUFcdTUyQThcdTVCRkNcdTUxNjVcdTc2ODRcdTYzRDJcdTRFRjZcclxuICAgICAgICAgZHRzOiAndHlwZXMvYXV0by1pbXBvcnRzLmQudHMnIC8vIFx1OEZEOVx1OTFDQ1x1NjYyRlx1NzUxRlx1NjIxMFx1NzY4NGdsb2JhbFx1NTFGRFx1NjU3MFx1NjU4N1xyXG4gICAgICB9KSxcclxuXHJcbiAgICAgIGVzbGludFBsdWdpbih7XHJcbiAgICAgICAgIGluY2x1ZGU6IFsnc3JjLyoqLyouanMnLCAnc3JjLyoqLyoudnVlJywgJ3NyYy8qKi8qLnRzJ10sIC8vIFx1NjMwN1x1NUI5QVx1OTcwMFx1ODk4MVx1NjhDMFx1NjdFNVx1NzY4NFx1NjU4N1x1NEVGNlxyXG4gICAgICAgICBleGNsdWRlOiBbJ25vZGVfbW9kdWxlcy8qKicsICdkaXN0LyoqJ10sIC8vIFx1NjMwN1x1NUI5QVx1NEUwRFx1OTcwMFx1ODk4MVx1NjhDMFx1NjdFNVx1NzY4NFx1NjU4N1x1NEVGNlxyXG4gICAgICAgICBmaXg6IGZhbHNlLCAvLyBcdTY2MkZcdTU0MjZcdTgxRUFcdTUyQThcdTRGRUVcdTU5MERcclxuICAgICAgICAgY2FjaGU6IGZhbHNlIC8vIFx1NjYyRlx1NTQyNlx1NTQyRlx1NzUyOFx1N0YxM1x1NUI1OFxyXG4gICAgICB9KVxyXG4gICBdLFxyXG4gICBkZWZpbmU6IHtcclxuICAgICAgUk9VVEVTOiBuZXcgVHJhbnNmb3JtUGFnZXMoKS5yb3V0ZXNcclxuICAgfSxcclxuICAgY3NzOiB7XHJcbiAgICAgIHByZXByb2Nlc3Nvck9wdGlvbnM6IHtcclxuICAgICAgICAgc2Nzczoge1xyXG4gICAgICAgICAgICBxdWlldERlcHJlY2F0aW9uczogWydhbGwnXVxyXG4gICAgICAgICB9XHJcbiAgICAgIH1cclxuICAgfSxcclxuICAgYnVpbGQ6IHtcclxuICAgICAgbWluaWZ5OiAndGVyc2VyJyxcclxuICAgICAgdGVyc2VyT3B0aW9uczoge1xyXG4gICAgICAgICBjb21wcmVzczoge1xyXG4gICAgICAgICAgICBkcm9wX2NvbnNvbGU6IHRydWUsXHJcbiAgICAgICAgICAgIGRyb3BfZGVidWdnZXI6IHRydWVcclxuICAgICAgICAgfVxyXG4gICAgICB9LFxyXG4gICAgICByb2xsdXBPcHRpb25zOiB7XHJcbiAgICAgICAgIHBsdWdpbnM6IFt0ZXJzZXIoKV1cclxuICAgICAgfVxyXG4gICB9LFxyXG4gICByZXNvbHZlOiB7XHJcbiAgICAgIGFsaWFzOiB7XHJcbiAgICAgICAgICdAJzogcmVzb2x2ZShfX2Rpcm5hbWUsICdzcmMnKVxyXG4gICAgICB9XHJcbiAgIH0sXHJcbiAgIHNlcnZlcjoge1xyXG4gICAgICBob3N0OiB0cnVlLCAvLyBcdTc2RDFcdTU0MkNcdTYyNDBcdTY3MDlcdTU3MzBcdTU3NDBcdUZGMENcdTUzMDVcdTYyRUNcdTY3MkNcdTY3M0EgSVBcclxuICAgICAgcG9ydDogNTE3MyxcclxuICAgICAgb3BlbjogdHJ1ZSwgLy8gXHU1M0VGXHU5MDA5XHVGRjFBXHU1NDJGXHU1MkE4XHU2NUY2XHU4MUVBXHU1MkE4XHU2MjUzXHU1RjAwXHU2RDRGXHU4OUM4XHU1NjY4XHJcbiAgICAgIHByb3h5OiB7XHJcbiAgICAgICAgICcvYXBpJzoge1xyXG4gICAgICAgICAgICB0YXJnZXQ6ICdodHRwOi8vbG9jYWxob3N0OjgwODAvJyxcclxuICAgICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxyXG4gICAgICAgICAgICByZXdyaXRlOiBwYXRoID0+IHBhdGgucmVwbGFjZSgvXlxcL2FwaS8sICcnKVxyXG4gICAgICAgICB9XHJcbiAgICAgIH1cclxuICAgfSxcclxuICAgLy8gXHU1NDJGXHU3NTI4IHRlcnNlciBcdTUzOEJcdTdGMjlcdUZGMDhcdTY2RkZcdTRFRTNcdTlFRDhcdThCQTRcdTc2ODQgZXNidWlsZFx1RkYwOVxyXG4gICBtaW5pZnk6ICd0ZXJzZXInLFxyXG4gICB0ZXJzZXJPcHRpb25zOiB7XHJcbiAgICAgIGNvbXByZXNzOiB7XHJcbiAgICAgICAgIC8vIFx1NTIyMFx1OTY2NFx1NjI0MFx1NjcwOSBjb25zb2xlLipcdUZGMDhcdTUzMDVcdTYyRUMgbG9nLCB3YXJuLCBlcnJvciBcdTdCNDlcdUZGMDlcclxuICAgICAgICAgZHJvcF9jb25zb2xlOiB0cnVlXHJcbiAgICAgICAgIC8vIFx1NTNFRlx1OTAwOVx1RkYxQVx1NTNFQVx1NTIyMFx1OTY2NCBjb25zb2xlLmxvZ1xyXG4gICAgICAgICAvLyBwdXJlX2Z1bmNzOiBbJ2NvbnNvbGUubG9nJ11cclxuICAgICAgfSxcclxuICAgICAgLy8gXHU1M0VGXHU5MDA5XHVGRjFBXHU1MjIwXHU5NjY0IGRlYnVnZ2VyIFx1OEJFRFx1NTNFNVxyXG4gICAgICBtYW5nbGU6IHRydWVcclxuICAgfVxyXG59KTtcclxuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUNBLFNBQVMsb0JBQW9CO0FBQzdCLE9BQU8sU0FBUztBQUNoQixTQUFTLGVBQWU7QUFDeEIsT0FBTyxnQkFBZ0I7QUFDdkIsU0FBUyxtQkFBbUI7QUFDNUIsT0FBTyxrQkFBa0I7QUFDekIsT0FBTyxpQkFBaUI7QUFDeEIsT0FBTyxvQkFBb0I7QUFDM0IsT0FBTyxnQkFBZ0I7QUFDdkIsU0FBUyxjQUFjO0FBVnZCLElBQU0sbUNBQW1DO0FBV3pDLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQ3pCLHVCQUF1QixDQUFDLFVBQVU7QUFBQSxFQUNsQyxTQUFTO0FBQUEsSUFDTixZQUFZO0FBQUEsSUFDWixJQUFJLFFBQVE7QUFBQSxJQUNaLFdBQVc7QUFBQSxNQUNSLFdBQVcsQ0FBQyxZQUFZLENBQUM7QUFBQSxJQUM1QixDQUFDO0FBQUEsSUFDRCxXQUFXO0FBQUEsTUFDUixTQUFTLENBQUMsT0FBTyxTQUFTO0FBQUE7QUFBQSxNQUMxQixLQUFLO0FBQUE7QUFBQSxJQUNSLENBQUM7QUFBQSxJQUVELGFBQWE7QUFBQSxNQUNWLFNBQVMsQ0FBQyxlQUFlLGdCQUFnQixhQUFhO0FBQUE7QUFBQSxNQUN0RCxTQUFTLENBQUMsbUJBQW1CLFNBQVM7QUFBQTtBQUFBLE1BQ3RDLEtBQUs7QUFBQTtBQUFBLE1BQ0wsT0FBTztBQUFBO0FBQUEsSUFDVixDQUFDO0FBQUEsRUFDSjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ0wsUUFBUSxJQUFJLGVBQWUsRUFBRTtBQUFBLEVBQ2hDO0FBQUEsRUFDQSxLQUFLO0FBQUEsSUFDRixxQkFBcUI7QUFBQSxNQUNsQixNQUFNO0FBQUEsUUFDSCxtQkFBbUIsQ0FBQyxLQUFLO0FBQUEsTUFDNUI7QUFBQSxJQUNIO0FBQUEsRUFDSDtBQUFBLEVBQ0EsT0FBTztBQUFBLElBQ0osUUFBUTtBQUFBLElBQ1IsZUFBZTtBQUFBLE1BQ1osVUFBVTtBQUFBLFFBQ1AsY0FBYztBQUFBLFFBQ2QsZUFBZTtBQUFBLE1BQ2xCO0FBQUEsSUFDSDtBQUFBLElBQ0EsZUFBZTtBQUFBLE1BQ1osU0FBUyxDQUFDLE9BQU8sQ0FBQztBQUFBLElBQ3JCO0FBQUEsRUFDSDtBQUFBLEVBQ0EsU0FBUztBQUFBLElBQ04sT0FBTztBQUFBLE1BQ0osS0FBSyxRQUFRLGtDQUFXLEtBQUs7QUFBQSxJQUNoQztBQUFBLEVBQ0g7QUFBQSxFQUNBLFFBQVE7QUFBQSxJQUNMLE1BQU07QUFBQTtBQUFBLElBQ04sTUFBTTtBQUFBLElBQ04sTUFBTTtBQUFBO0FBQUEsSUFDTixPQUFPO0FBQUEsTUFDSixRQUFRO0FBQUEsUUFDTCxRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsUUFDZCxTQUFTLFVBQVEsS0FBSyxRQUFRLFVBQVUsRUFBRTtBQUFBLE1BQzdDO0FBQUEsSUFDSDtBQUFBLEVBQ0g7QUFBQTtBQUFBLEVBRUEsUUFBUTtBQUFBLEVBQ1IsZUFBZTtBQUFBLElBQ1osVUFBVTtBQUFBO0FBQUEsTUFFUCxjQUFjO0FBQUE7QUFBQTtBQUFBLElBR2pCO0FBQUE7QUFBQSxJQUVBLFFBQVE7QUFBQSxFQUNYO0FBQ0gsQ0FBQzsiLAogICJuYW1lcyI6IFtdCn0K
