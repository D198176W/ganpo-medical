package com.atguigu.consult.retrieval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 中文分词器
 * 采用"医疗领域词典最大匹配 + 字符Bigram"混合策略
 *
 * 分词策略：
 * 1. 维护医疗领域高频术语词典（医院名、科室名、症状词等）
 * 2. 对输入文本进行正向最大匹配分词
 * 3. 词典未覆盖部分采用2-gram字符分词，保证召回率
 */
public class ChineseTokenizer {

    // 医疗领域词典（按长度降序排列，保证最大匹配）
    private static final List<String> MEDICAL_DICTIONARY = Arrays.asList(
            // 医院名称
            "南昌大学第一附属医院", "南昌大学第二附属医院", "江西中医药大学附属医院",
            "江西省人民医院", "南昌市第一医院", "南昌市第三医院",
            // 科室名称
            "呼吸与危重症医学科", "心血管内科", "神经内科", "消化内科",
            "呼吸内科", "骨科", "皮肤科", "眼科", "口腔科", "妇产科", "儿科",
            "内分泌科", "急诊科", "心内科", "呼吸科",
            // 疾病名称
            "上呼吸道感染", "慢性阻塞性肺疾病", "腰椎间盘突出", "糖尿病",
            "高血压", "冠心病", "偏头痛", "颈椎病", "湿疹", "荨麻疹",
            "急性胃肠炎", "支气管哮喘", "肺结核", "肺炎",
            // 症状词汇
            "呼吸困难", "记忆力下降", "视物模糊", "关节疼痛",
            "头痛", "头晕", "发热", "咳嗽", "胸痛", "腹痛", "腹泻",
            "恶心", "呕吐", "乏力", "麻木", "瘙痒", "皮疹", "失眠",
            "心慌", "气短", "胸闷", "气喘", "咳痰", "便血", "反酸",
            // 医保相关
            "医保定点", "异地就医", "报销比例", "门诊特殊慢性病",
            "医保", "报销", "备案", "定点", "慢病",
            // 医疗术语
            "挂号", "预约", "号源", "门诊", "急诊", "住院", "检查",
            "血常规", "胸部CT", "心电图", "心脏彩超", "胃镜", "肠镜",
            "处方", "剂量", "疗程"
    );

    // 按长度降序排列，保证最大匹配
    private static final List<String> SORTED_DICT;
    static {
        SORTED_DICT = new ArrayList<>(MEDICAL_DICTIONARY);
        SORTED_DICT.sort((a, b) -> b.length() - a.length());
    }

    // 停用词
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "的", "了", "是", "在", "我", "有", "和", "就", "不", "人", "都",
            "一", "一个", "上", "也", "很", "到", "说", "要", "去", "你",
            "会", "着", "没有", "看", "好", "自己", "这", "那", "怎么",
            "什么", "吗", "呢", "啊", "吧", "最近", "总是", "有点", "可能"
    ));

    /**
     * 对文本进行分词
     *
     * @param text 输入文本
     * @return 分词结果列表
     */
    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // 预处理：去除标点符号和特殊字符，保留中文、英文、数字
        String cleaned = text.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9]", " ");
        List<String> result = new ArrayList<>();

        // 第一遍：词典最大匹配
        result.addAll(maxMatchSegment(cleaned));

        // 过滤停用词
        result.removeIf(token -> STOP_WORDS.contains(token) || token.trim().isEmpty());

        return result;
    }

    /**
     * 正向最大匹配分词
     */
    private List<String> maxMatchSegment(String text) {
        List<String> tokens = new ArrayList<>();
        int pos = 0;
        int maxWordLen = SORTED_DICT.isEmpty() ? 1 : SORTED_DICT.get(0).length();

        while (pos < text.length()) {
            // 跳过空格
            if (text.charAt(pos) == ' ') {
                pos++;
                continue;
            }

            boolean matched = false;
            // 从最长到最短尝试匹配词典
            int end = Math.min(pos + maxWordLen, text.length());
            for (int i = end; i > pos; i--) {
                String sub = text.substring(pos, i);
                if (SORTED_DICT.contains(sub)) {
                    tokens.add(sub);
                    pos = i;
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                // 词典未匹配，采用单字+bigram策略
                char c = text.charAt(pos);
                if (isChineseChar(c)) {
                    // 添加单字
                    tokens.add(String.valueOf(c));
                    // 添加bigram（当前字和下一个字的组合）
                    if (pos + 1 < text.length() && isChineseChar(text.charAt(pos + 1))) {
                        String bigram = text.substring(pos, pos + 2);
                        tokens.add(bigram);
                    }
                } else if (Character.isLetterOrDigit(c)) {
                    // 英文/数字连续序列作为一个token
                    int start = pos;
                    while (pos < text.length() && 
                           (Character.isLetterOrDigit(text.charAt(pos)) || text.charAt(pos) == '.')) {
                        pos++;
                    }
                    tokens.add(text.substring(start, pos).toLowerCase());
                    continue;
                }
                pos++;
            }
        }

        return tokens;
    }

    private boolean isChineseChar(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }
}
