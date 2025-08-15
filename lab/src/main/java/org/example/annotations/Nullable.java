package org.example.annotations;

/**
 * 该接口标注的字段可以为空，或方法可能返回空
 */
public @interface Nullable {
    String value() default "";
}
