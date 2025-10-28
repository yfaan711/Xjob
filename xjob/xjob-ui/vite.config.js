import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // 代理配置，解决跨域问题
    proxy: {
      '/api': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        // 开启代理日志调试
        logLevel: 'debug'
      }
    },
    // 增加服务器调试日志
    debug: true
  },
  // 增加构建日志
  logLevel: 'info'
})
