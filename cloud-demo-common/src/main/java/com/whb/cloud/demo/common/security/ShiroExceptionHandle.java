package com.whb.cloud.demo.common.security;

import com.whb.cloud.demo.common.model.ResultVO;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ShiroExceptionHandle {

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public ResultVO authorizationException(){
        return ResultVO.fail("没有权限执行该接口");
    }
}
