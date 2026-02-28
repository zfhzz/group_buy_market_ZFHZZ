package cn.bugstack.types.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) //标签存在的时间
@Target({ElementType.FIELD}) //标签贴在哪里,FIELD表示贴在属性上,type表示贴在类上,method表示贴在方法上
@Documented
public @interface DCCValue {
    String value() default "";
}
