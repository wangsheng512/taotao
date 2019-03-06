package com.taotao.manage.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat>{
	
	/*@Autowired
	private ItemCatMapper itemCatMapper;*/

	/*public List<ItemCat> queryItemCatListByParentId(Long pid) {
		ItemCat record = new ItemCat();
		record.setParentId(pid);//查询参数
		return this.itemCatMapper.select(record);
	}*/
/*
	@Override
	public Mapper<ItemCat> getMapper() {
		return this.itemCatMapper;
	}*/
	
	private static final ObjectMapper Mapper = new ObjectMapper();
	
	private static final String REDIS_KEY = "TAOTAO_MANAGE_ITEM_CAT_API";//规则：项目名_模块名_业务名
	
	private static final Integer REDIS_TIME = 60*60*24*30*3;//生存时间
	
	@Autowired
	private RedisService redisService;
	
	/**
	 * 全部查询，并且生成树状结构
	 * @return
	 */
	public ItemCatResult queryAllToTree() {
		ItemCatResult result = new ItemCatResult();
		
		try {
			//先从缓存总命中，如果命中就返回，没有命中就继续执行
			String cacheDate = this.redisService.get(REDIS_KEY);
			if(StringUtils.isNotEmpty(cacheDate)){
				//命中
				//反序列化，自定义的对象
					return Mapper.readValue(cacheDate, ItemCatResult.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// 全部查出，并且在内存中生成树形结构
		List<ItemCat> cats = super.queryAll();
		
		// 转为map存储，key为父节点ID，value为数据集合
		Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
		for (ItemCat itemCat : cats) {
			if(!itemCatMap.containsKey(itemCat.getParentId())){
				itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}
			itemCatMap.get(itemCat.getParentId()).add(itemCat);
		}
		
		// 封装一级对象
		List<ItemCat> itemCatList1 = itemCatMap.get(0L);
		for (ItemCat itemCat : itemCatList1) {
			ItemCatData itemCatData = new ItemCatData();
			itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
			itemCatData.setName("<a href='"+itemCatData.getUrl()+"'>"+itemCat.getName()+"</a>");
			result.getItemCats().add(itemCatData);
			if(!itemCat.getIsParent()){
				continue;
			}
			
			// 封装二级对象
			List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
			List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
			itemCatData.setItems(itemCatData2);
			for (ItemCat itemCat2 : itemCatList2) {
				ItemCatData id2 = new ItemCatData();
				id2.setName(itemCat2.getName());
				id2.setUrl("/products/" + itemCat2.getId() + ".html");
				itemCatData2.add(id2);
				if(itemCat2.getIsParent()){
					// 封装三级对象
					List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
					List<String> itemCatData3 = new ArrayList<String>();
					id2.setItems(itemCatData3);
					for (ItemCat itemCat3 : itemCatList3) {
						itemCatData3.add("/products/" + itemCat3.getId() + ".html|"+itemCat3.getName());
					}
				}
			}
			if(result.getItemCats().size() >= 14){
				break;
			}
		}
		
		//将数据查询的结果集写入缓存中
		try {
			this.redisService.set(REDIS_KEY, Mapper.writeValueAsString(result), REDIS_TIME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
