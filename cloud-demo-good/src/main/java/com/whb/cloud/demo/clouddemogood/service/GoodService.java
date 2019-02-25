package com.whb.cloud.demo.clouddemogood.service;

import com.whb.cloud.demo.clouddemogood.model.GoodModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodService {
    private Map<Integer, GoodModel> goodModelMap = new HashMap<>();
    {
        GoodModel goodModel = new GoodModel(1,"啤酒","雪花");
        goodModelMap.put(goodModel.getId(), goodModel);
        goodModel = new GoodModel(2,"啤酒","雪花");
        goodModelMap.put(goodModel.getId(), goodModel);
        goodModel = new GoodModel(3,"啤酒","纯生");
        goodModelMap.put(goodModel.getId(), goodModel);
        goodModel = new GoodModel(4,"雷碧","雷碧");
        goodModelMap.put(goodModel.getId(), goodModel);
        goodModel = new GoodModel(5,"果粒橙","美汁源");
        goodModelMap.put(goodModel.getId(), goodModel);
        goodModel = new GoodModel(6,"矿泉水","康帅傅");
        goodModelMap.put(goodModel.getId(), goodModel);
    }

    /**
     * 获取所有的商品
     * @return
     */
    public List<GoodModel> getAll(){
        return new ArrayList<>(goodModelMap.values());
    }
    /**
     * 获取指定商品
     * @return
     */
    public GoodModel get(Integer id){
        if(id==null){
            throw new RuntimeException("商品信息不存在");
        }
        return goodModelMap.get(id);
    }

    /**
     * 更新商品信息
     * @param goodModel
     * @return
     */
    public GoodModel update(GoodModel goodModel){
        Integer id = goodModel.getId();
        if(id==null || goodModelMap.get(id)==null){
            throw new RuntimeException("商品信息不存在");
        }
        goodModelMap.put(id, goodModel);
        return goodModel;
    }

    /**
     * 删除指定商品
     * @param id
     */
    public void delete(Integer id){
        if(id==null){
            throw new RuntimeException("商品信息不存在");
        }
        goodModelMap.remove(id);
    }

    /**
     * 添加指定商品
     * @param goodModel
     * @return
     */
    public GoodModel add(GoodModel goodModel){
        Integer id = goodModel.getId();
        if(goodModelMap.get(id) != null){
            throw new RuntimeException("商品编号信息重复");
        }
        goodModelMap.put(id, goodModel);
        return goodModel;
    }
}
