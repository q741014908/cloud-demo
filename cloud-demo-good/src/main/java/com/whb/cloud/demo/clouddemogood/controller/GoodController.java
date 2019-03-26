package com.whb.cloud.demo.clouddemogood.controller;

import com.whb.cloud.demo.clouddemogood.model.GoodModel;
import com.whb.cloud.demo.clouddemogood.service.GoodService;
import com.whb.cloud.demo.common.model.ResultVO;
import com.whb.cloud.demo.common.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("goods")
@Slf4j
public class GoodController {
    @Autowired
    private GoodService goodService;

    @GetMapping
    @ResponseBody
    @RequiresRoles("ROLE_SELECT")
    public ResultVO<List<GoodModel>> get(){
        log.info("shiro中的名称:{}",SecurityUtils.getSubject().getPrincipals().asList());
        log.info("request中的用户:{}", RequestUtils.getCurrentRequestUserInfo());
        return ResultVO.success(goodService.getAll());
    }

    @GetMapping("/{id}")
    @ResponseBody
    @RequiresRoles("ROLE_SELECT")
    public ResultVO<GoodModel> get(@PathVariable("id") Integer id){
        return ResultVO.success(goodService.get(id));
    }

    @PostMapping
    @ResponseBody
    public ResultVO<GoodModel> add(@RequestBody GoodModel goodModel){
        return ResultVO.success(goodService.add(goodModel));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @RequiresRoles("ROLE_DELETE")
    public ResultVO delete(@PathVariable Integer id){
        goodService.delete(id);
        return ResultVO.success();
    }

    @PutMapping
    @ResponseBody
    @RequiresRoles("ROLE_UPDATE")
    public ResultVO put(@RequestBody GoodModel goodModel){
        goodService.update(goodModel);
        return ResultVO.success(goodModel);
    }

}
