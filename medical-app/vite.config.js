// vite.config.js
<<<<<<< HEAD
=======
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



>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src') // 别名配置，保持原有逻辑
    }
<<<<<<< HEAD
=======
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        secure: false
      }
    }
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
  }


})



<<<<<<< HEAD


=======
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
