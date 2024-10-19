package com.kc.lesson.permission;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface RolePermission {
    RolePermissionLevel level() default RolePermissionLevel.LOGIN; //默认为ALL
}
