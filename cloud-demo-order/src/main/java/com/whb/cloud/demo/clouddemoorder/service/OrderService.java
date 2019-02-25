package com.whb.cloud.demo.clouddemoorder.service;

import com.whb.cloud.demo.clouddemoorder.model.GoodModel;
import com.whb.cloud.demo.clouddemoorder.model.OrderModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private Map<Integer, OrderModel> orderModelMap = new HashMap<>();

    {
        GoodModel goodModel1 = new GoodModel(1,"啤酒","雪花");
        GoodModel goodModel2 = new GoodModel(2,"啤酒","雪花");
        GoodModel goodModel3 = new GoodModel(3,"啤酒","纯生");
        GoodModel goodModel4 = new GoodModel(4,"雷碧","雷碧");
        GoodModel goodModel5 = new GoodModel(5,"果粒橙","美汁源");
        GoodModel goodModel6 = new GoodModel(6,"矿泉水","康帅傅");

        OrderModel orderModel=new OrderModel(1,"未支付", Arrays.asList(new GoodModel[]{goodModel1,goodModel3,goodModel5}));
        orderModelMap.put(orderModel.getId(), orderModel);
        orderModel=new OrderModel(2,"已支付", Arrays.asList(new GoodModel[]{goodModel1,goodModel2}));
        orderModelMap.put(orderModel.getId(), orderModel);
        orderModel=new OrderModel(3,"已发货", Arrays.asList(new GoodModel[]{goodModel3,goodModel4,goodModel5}));
        orderModelMap.put(orderModel.getId(), orderModel);
        orderModel=new OrderModel(4,"未支付", Arrays.asList(new GoodModel[]{goodModel2,goodModel3,goodModel5}));
        orderModelMap.put(orderModel.getId(), orderModel);
        orderModel=new OrderModel(5,"已发货", Arrays.asList(new GoodModel[]{goodModel1,goodModel2,goodModel4,goodModel5}));
        orderModelMap.put(orderModel.getId(), orderModel);
        orderModel=new OrderModel(6,"已完成", Arrays.asList(new GoodModel[]{goodModel1,goodModel4,goodModel5}));
        orderModelMap.put(orderModel.getId(), orderModel);
    }


    /**
     * 获取所有的商品
     * @return
     */
    public List<OrderModel> getAll(){
        return new ArrayList<>(orderModelMap.values());
    }
    /**
     * 获取指定商品
     * @return
     */
    public OrderModel get(Integer id){
        if(id==null){
            throw new RuntimeException("商品信息不存在");
        }
        return orderModelMap.get(id);
    }

    /**
     * 更新商品信息
     * @param orderModel
     * @return
     */
    public OrderModel update(OrderModel orderModel){
        Integer id = orderModel.getId();
        if(id==null || orderModelMap.get(id)==null){
            throw new RuntimeException("商品信息不存在");
        }
        orderModelMap.put(id, orderModel);
        return orderModel;
    }

    /**
     * 删除指定商品
     * @param id
     */
    public void delete(Integer id){
        if(id==null){
            throw new RuntimeException("商品信息不存在");
        }
        orderModelMap.remove(id);
    }

    /**
     * 添加指定商品
     * @param orderModel
     * @return
     */
    public OrderModel add(OrderModel orderModel){
        Integer id = orderModel.getId();
        if(orderModelMap.get(id) != null){
            throw new RuntimeException("商品编号信息重复");
        }
        orderModelMap.put(id, orderModel);
        return orderModel;
    }
}
