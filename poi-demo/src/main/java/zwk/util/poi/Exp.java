package zwk.util.poi;

import java.lang.annotation.*;

/**
 * excel导出 自定义注解
 * @author zwk
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exp {

    /**
     * 列名
     */
    String name();

    /**
     * 列宽
     */
    int width() default 10;

}
