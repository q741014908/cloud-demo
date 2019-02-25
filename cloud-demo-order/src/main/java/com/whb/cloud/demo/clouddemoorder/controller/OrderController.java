package com.whb.cloud.demo.clouddemoorder.controller;

import com.whb.cloud.demo.clouddemoorder.model.GoodModel;
import com.whb.cloud.demo.clouddemoorder.model.OrderModel;
import com.whb.cloud.demo.clouddemoorder.service.OrderService;
import com.whb.cloud.demo.common.model.ResultVO;
import com.whb.cloud.demo.common.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String GOOD_SERVICE_URI = "http://demo-good";

    @GetMapping
    @ResponseBody
    @RequiresRoles("ROLE_select")
    public ResultVO<List<OrderModel>> get(){
        ResultVO forObject = restTemplate.getForObject(GOOD_SERVICE_URI + "/goods", ResultVO.class);
        log.info("订单服务获取到的商品信息:{}",forObject);
        return ResultVO.success(orderService.getAll());
    }

    @GetMapping("/{id}")
    @ResponseBody
    @RequiresRoles("ROLE_select")
    public ResultVO<OrderModel> get(@PathVariable("id") Integer id){
        return ResultVO.success(orderService.get(id));
    }

    @PostMapping
    @ResponseBody
    public ResultVO<OrderModel> add(@RequestBody OrderModel orderModel){
        return ResultVO.success(orderService.add(orderModel));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @RequiresRoles("ROLE_delete")
    public ResultVO delete(@PathVariable Integer id){
        orderService.delete(id);
        return ResultVO.success();
    }

    @PutMapping
    @ResponseBody
    @RequiresRoles("ROLE_update")
    public ResultVO put(@RequestBody OrderModel orderModel){
        orderService.update(orderModel);
        return ResultVO.success(orderModel);
    }

}
