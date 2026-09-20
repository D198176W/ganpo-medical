// vite.config.js

// import { defineConfig } from 'vite'
// import vue from '@vitejs/plugin-vue'
// import { resolve } from 'path'

// export default defineConfig({
//   plugins: [vue()],
//   resolve: {
//     alias: {
//       '@': resolve(__dirname, 'src') // 别名配置，保持原有逻辑
//     }
//   }


// })




import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src') // 别名配置，保持原有逻辑
    }

  },
  server: {
    proxy: {
      '/api/chat': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        secure: false
      },
      '/api/knowledge': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        secure: false
      },
      '/api/images': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        secure: false
      },
      '/api': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        secure: false
      },
      '/xiaozhi': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }

  }


})



