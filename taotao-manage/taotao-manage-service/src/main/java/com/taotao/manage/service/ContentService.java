package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content>{
	
	@Autowired
	private ContentMapper contentMapper;
	
	public EasyUIResult queryListByCategoryId(Long categoryId, Integer page, Integer rows) {
		//分页
		PageHelper.startPage(page, rows);
		List<Content> list = this.contentMapper.queryContentList(categoryId);
		PageInfo<Content> pageInfo = new PageInfo<>(list);
		return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
	}

}
