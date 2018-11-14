package springboot.core.anno;

import java.lang.annotation.*;

/**
 * 标记是否将字段转换为map，MapUtil使用
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotToMap {

}

