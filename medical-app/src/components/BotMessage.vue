<template>
  <!-- 生成式 UI：根据 Agent 输出内容动态组织界面组件，而非固定文本气泡 -->
  <div class="bot-message">
    <template v-for="(block, idx) in blocks" :key="idx">

      <!-- 紧急警告 → 红色警示卡片 -->
      <div v-if="block.type === 'alert'" class="alert-card">
        <span class="alert-icon">🚨</span>
        <span v-html="inlineFormat(block.text)"></span>
      </div>

      <!-- Markdown 表格 → 真实表格组件 -->
      <div v-else-if="block.type === 'table'" class="table-card">
        <table>
          <thead>
            <tr>
              <th v-for="(h, i) in block.headers" :key="i" v-html="inlineFormat(h)"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, r) in block.rows" :key="r">
              <td v-for="(cell, c) in row" :key="c" v-html="inlineFormat(cell)"></td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 标题 → 分节标题 -->
      <div v-else-if="block.type === 'heading'" class="msg-heading" :class="'h' + block.level">
        <span v-html="inlineFormat(block.text)"></span>
      </div>

      <!-- 列表 -->
      <ul v-else-if="block.type === 'list'" class="msg-list">
        <li v-for="(item, i) in block.items" :key="i" v-html="inlineFormat(item)"></li>
      </ul>
      <ol v-else-if="block.type === 'ordered-list'" class="msg-list ordered">
        <li v-for="(item, i) in block.items" :key="i" v-html="inlineFormat(item)"></li>
      </ol>

      <!-- 免责声明 → 弱化脚注 -->
      <div v-else-if="block.type === 'disclaimer'" class="disclaimer">
        <span v-html="inlineFormat(block.text)"></span>
      </div>

      <!-- 普通段落 -->
      <p v-else class="msg-paragraph" v-html="inlineFormat(block.text)"></p>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  text: { type: String, default: '' }
})

/**
 * 将 Agent 的 Markdown 输出解析为结构化块，驱动动态组件渲染。
 * 流式场景下每次 token 更新都会重新解析，未闭合的表格/列表会自然补齐。
 */
const blocks = computed(() => parseBlocks(props.text || ''))

function parseBlocks(text) {
  const lines = text.split('\n')
  const result = []
  let i = 0
  while (i < lines.length) {
    const line = lines[i]
    const trimmed = line.trim()

    // 空行 / 分隔线
    if (!trimmed || /^-{3,}$/.test(trimmed)) { i++; continue }

    // 表格块（连续 | 开头的行）
    if (trimmed.startsWith('|')) {
      const tableLines = []
      while (i < lines.length && lines[i].trim().startsWith('|')) {
        tableLines.push(lines[i].trim())
        i++
      }
      const rows = tableLines
        .filter(l => !/^\|[\s:|-]+\|$/.test(l)) // 跳过分隔线
        .map(l => l.split('|').slice(1, -1).map(c => c.trim()))
      if (rows.length > 0) {
        result.push({ type: 'table', headers: rows[0], rows: rows.slice(1) })
      }
      continue
    }

    // 标题
    const headingMatch = trimmed.match(/^(#{1,4})\s+(.*)$/)
    if (headingMatch) {
      result.push({ type: 'heading', level: headingMatch[1].length, text: headingMatch[2] })
      i++
      continue
    }

    // 无序列表
    if (/^[-*]\s+/.test(trimmed)) {
      const items = []
      while (i < lines.length && /^[-*]\s+/.test(lines[i].trim())) {
        items.push(lines[i].trim().replace(/^[-*]\s+/, ''))
        i++
      }
      result.push({ type: 'list', items })
      continue
    }

    // 有序列表
    if (/^\d+\.\s+/.test(trimmed)) {
      const items = []
      while (i < lines.length && /^\d+\.\s+/.test(lines[i].trim())) {
        items.push(lines[i].trim().replace(/^\d+\.\s+/, ''))
        i++
      }
      result.push({ type: 'ordered-list', items })
      continue
    }

    // 段落（合并连续普通行）
    const paraLines = []
    while (i < lines.length) {
      const t = lines[i].trim()
      if (!t || t.startsWith('|') || /^#{1,4}\s/.test(t) || /^[-*]\s+/.test(t) || /^\d+\.\s+/.test(t) || /^-{3,}$/.test(t)) break
      paraLines.push(t)
      i++
    }
    const para = paraLines.join(' ')
    if (para) {
      if (/[🚨⚠️]/.test(paraLines[0] || '') || /紧急警告|紧急提示/.test(para)) {
        result.push({ type: 'alert', text: para })
      } else if (/仅供参考|不能替代专业医疗诊断|遵医嘱/.test(para)) {
        result.push({ type: 'disclaimer', text: para })
      } else {
        result.push({ type: 'paragraph', text: para })
      }
    }
  }
  return result
}

/** 行内格式：转义 HTML 后还原 **加粗** 与 `代码` */
function inlineFormat(text) {
  if (!text) return ''
  const escaped = text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
  return escaped
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/`(.+?)`/g, '<code>$1</code>')
}
</script>

<style scoped>
.bot-message {
  display: flex;
  flex-direction: column;
  gap: 8px;
  line-height: 1.6;
}

/* 紧急警告卡片 */
.alert-card {
  display: flex;
  gap: 8px;
  align-items: flex-start;
  background: #fff1f0;
  border: 1px solid #ffccc7;
  border-left: 4px solid #ff4d4f;
  border-radius: 8px;
  padding: 10px 12px;
  color: #cf1322;
  font-weight: 500;
}
.alert-icon { flex-shrink: 0; }

/* 表格卡片 */
.table-card {
  overflow-x: auto;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  background: #fff;
}
.table-card table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.88rem;
}
.table-card th {
  background: #f0f7ff;
  color: #2f54eb;
  font-weight: 600;
  text-align: left;
  padding: 8px 12px;
  border-bottom: 2px solid #d6e4ff;
  white-space: nowrap;
}
.table-card td {
  padding: 8px 12px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}
.table-card tr:last-child td { border-bottom: none; }
.table-card tr:hover td { background: #fafcff; }

/* 分节标题 */
.msg-heading {
  font-weight: 600;
  color: #1d39c4;
  margin-top: 4px;
}
.msg-heading.h2 { font-size: 1rem; }
.msg-heading.h3, .msg-heading.h4 { font-size: 0.92rem; color: #2f54eb; }

/* 列表 */
.msg-list {
  padding-left: 18px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 0.92rem;
}
.msg-list.ordered { list-style: decimal; }

/* 免责声明脚注 */
.disclaimer {
  font-size: 0.78rem;
  color: #999;
  border-top: 1px dashed #e0e0e0;
  padding-top: 6px;
}

/* 段落 */
.msg-paragraph {
  font-size: 0.92rem;
  color: #333;
  white-space: pre-wrap;
}

code {
  background: #f5f5f5;
  border-radius: 4px;
  padding: 1px 5px;
  font-size: 0.85em;
  color: #c41d7f;
}
</style>
