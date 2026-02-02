package org.example.annotations;

/**
 * 该注解作用的方法参数时表示该参数可以为空
 */
public @interface Optional {
    String description() default "";
}
