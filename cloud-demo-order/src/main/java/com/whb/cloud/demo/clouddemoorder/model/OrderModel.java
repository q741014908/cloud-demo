package com.whb.cloud.demo.clouddemoorder.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderModel {

    private Integer id;

    /**
     * 订单状态
     */
    private String state;
    /**
     * 多个商品信息
     */
    private List<GoodModel> goodModels;

}
