package com.atguigu.consult.router;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 多模型路由单元测试
 */
class ModelRouterTest {

    private final ModelRouter router = new ModelRouter();

    @Test
    void simpleQuestionGoesLite() {
        assertEquals(ModelRouter.ModelTier.LITE, router.route("你好"));
        assertEquals(ModelRouter.ModelTier.LITE, router.route("医院几点上班"));
        assertEquals(ModelRouter.ModelTier.LITE, router.route(null));
    }

    @Test
    void symptomQuestionGoesPrimary() {
        assertEquals(ModelRouter.ModelTier.PRIMARY, router.route("我胸痛怎么办"));
        assertEquals(ModelRouter.ModelTier.PRIMARY, router.route("高血压怎么治疗"));
    }

    @Test
    void longQuestionGoesPrimary() {
        String longQuestion = "我最近两周每天下午都觉得不舒服，而且情况一天比一天严重，还影响了正常上课，想问问应该怎么办才好";
        assertEquals(ModelRouter.ModelTier.PRIMARY, router.route(longQuestion));
    }

    @Test
    void emergencyGoesPrimary() {
        assertEquals(ModelRouter.ModelTier.PRIMARY, router.route("急救电话是多少"));
    }
}
