package com.taotao.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;

@RequestMapping("order")
@Controller
public class OrderController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private OrderService orderService;
	
	
	/**
	 * 订单确认页
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}" , method = RequestMethod.GET)
	public ModelAndView toOrder(@PathVariable("itemId") Long itemId){
		ModelAndView mv = new ModelAndView("order");
		Item item = this.itemService.queryById(itemId);
		mv.addObject("item", item);
		return mv;
	}
	
	/**
	 * 提交订单
	 * @param order//,
			@CookieValue(UserLoginHandlerInterceptor.COOKIE_NAME) String token
	 * @return
	 */
	@RequestMapping(value = "submit" , method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submit(Order order){
		Map<String, Object> result = new HashMap<String,Object>();
		
		String orderId = this.orderService.submit(order);
		if(StringUtils.isEmpty(orderId)){
			//订单，提交失败
			result.put("status", 500);
		}else{
			result.put("status", 200);
			result.put("data", orderId);
		}
		return result;
	}
	
	/**
	 * 订单成功页面
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "success" ,method = RequestMethod.GET)
	public ModelAndView success(@RequestParam("id") String orderId){
		ModelAndView mv = new ModelAndView("success");
		//订单的数据
		Order order = this.orderService.queryByOrderId(orderId);
		mv.addObject("order", order);
		//送货时间
		mv.addObject("data", new DateTime().plusDays(2).toString("MM月dd日"));
		return mv;
	}
}
