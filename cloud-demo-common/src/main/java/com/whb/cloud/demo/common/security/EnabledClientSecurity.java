package com.whb.cloud.demo.common.security;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WebSecurityConfig.class,ShiroConfiguration.class,ShiroExceptionHandle.class})
public @interface EnabledClientSecurity {
}
