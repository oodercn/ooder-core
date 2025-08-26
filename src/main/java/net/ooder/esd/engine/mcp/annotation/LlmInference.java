package net.ooder.esd.engine.mcp.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * LLM推理服务配置注解
 * 用于标记CloudWeGo服务并配置LLM模型参数
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface LlmInference {
    /**
     * LLM模型名称
     */
    String model() default "default";

    /**
     * 推理超时时间(ms)
     */
    int timeout() default 30000;

    /**
     * 最大输入 tokens
     */
    int maxInputTokens() default 2048;

    /**
     * 最大输出 tokens
     */
    int maxOutputTokens() default 1024;

    /**
     * 温度参数，控制随机性 (0.0-1.0)
     */
    float temperature() default 0.7f;

    /**
     * CloudWeGo服务分组
     */
    String group() default "llm";

    /**
     * CloudWeGo服务版本
     */
    String version() default "1.0.0";
}