import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

// 在 ES 模块中获取 __dirname 的等价物
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

class AssetFixer {
  constructor() {
    this.missingAssets = [];
  }

  // 扫描所有 Vue 文件中的资源引用
  scanVueFiles(dir = 'src') {
    const vueFiles = this.findFiles(dir, '.vue');
    
    vueFiles.forEach(file => {
      const content = fs.readFileSync(file, 'utf8');
      this.extractAssets(content, file);
    });
  }

  // 查找文件
  findFiles(dir, ext) {
    let results = [];
    const items = fs.readdirSync(dir);
    
    items.forEach(item => {
      const itemPath = path.join(dir, item);
      const stat = fs.statSync(itemPath);
      
      if (stat && stat.isDirectory()) {
        results = results.concat(this.findFiles(itemPath, ext));
      } else if (item.endsWith(ext)) {
        results.push(itemPath);
      }
    });
    
    return results;
  }

  // 提取资源引用
  extractAssets(content, filePath) {
    // 匹配 @/assets/ 引用
    const assetRegex = /(@\/assets\/[^'"]*)/g;
    const matches = content.match(assetRegex) || [];
    
    matches.forEach(assetRef => {
      const relativePath = assetRef.replace('@/', '');
      const fullPath = path.join('src', relativePath);
      
      if (!fs.existsSync(fullPath)) {
        this.missingAssets.push({
          file: filePath,
          asset: assetRef,
          fullPath: fullPath
        });
      }
    });
  }

  // 修复缺失资源
  fixMissingAssets() {
    this.missingAssets.forEach(({ asset, fullPath }) => {
      const dir = path.dirname(fullPath);
      
      // 创建目录
      if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
      }
      
      // 根据文件类型创建占位文件
      const ext = path.extname(fullPath).toLowerCase();
      this.createPlaceholderFile(fullPath, ext);
    });
  }

  // 创建占位文件
  createPlaceholderFile(filePath, ext) {
    switch (ext) {
      case '.png':
      case '.jpg':
      case '.jpeg':
        this.createImagePlaceholder(filePath);
        break;
      case '.svg':
        this.createSVGPlaceholder(filePath);
        break;
      default:
        fs.writeFileSync(filePath, `// Placeholder for ${path.basename(filePath)}`);
    }
    console.log(`✅ 创建占位文件: ${filePath}`);
  }

  createImagePlaceholder(filePath) {
    // 简单的1x1透明PNG
    const pngData = Buffer.from([
      0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D,
      0x49, 0x48, 0x44, 0x52, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
      0x08, 0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, 0xC4, 0x89, 0x00, 0x00, 0x00,
      0x0A, 0x49, 0x44, 0x41, 0x54, 0x78, 0x9C, 0x63, 0x00, 0x01, 0x00, 0x00,
      0x05, 0x00, 0x01, 0x0D, 0x0A, 0x2D, 0xB4, 0x00, 0x00, 0x00, 0x00, 0x49,
      0x45, 0x4E, 0x44, 0xAE, 0x42, 0x60, 0x82
    ]);
    fs.writeFileSync(filePath, pngData);
  }

  createSVGPlaceholder(filePath) {
    const svgContent = `<svg width="100" height="100" xmlns="http://www.w3.org/2000/svg">
      <rect width="100" height="100" fill="#f0f0f0" stroke="#ccc"/>
      <text x="50" y="50" text-anchor="middle" dy=".3em" fill="#999">${path.basename(filePath)}</text>
    </svg>`;
    fs.writeFileSync(filePath, svgContent);
  }

  // 生成报告
  generateReport() {
    if (this.missingAssets.length === 0) {
      console.log('🎉 没有发现缺失的资源文件！');
      return;
    }

    console.log('\n📋 缺失资源报告:');
    this.missingAssets.forEach((asset, index) => {
      console.log(`${index + 1}. 文件: ${asset.file}`);
      console.log(`   引用: ${asset.asset}`);
      console.log(`   路径: ${asset.fullPath}`);
    });
  }

  run() {
    console.log('🔍 扫描资源文件...');
    this.scanVueFiles();
    
    if (this.missingAssets.length > 0) {
      this.generateReport();
      console.log('\n🛠️ 开始修复...');
      this.fixMissingAssets();
      console.log('\n🎉 修复完成！');
    } else {
      console.log('✅ 没有发现缺失的资源文件');
    }
  }
}

// 运行修复
const fixer = new AssetFixer();
fixer.run();