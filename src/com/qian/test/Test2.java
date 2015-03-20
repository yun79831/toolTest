package com.qian.test;

import com.tools.MD5;

public class Test2 {
public static void main(String[] args) {
//	//必须保证长度相同才能出正确结果
//	System.out.println("神马浮".compareTo("神马浮"));
//	
//	if("20140411".compareTo("20140101")>=0 && "20140411".compareTo("20140411")<=0){
//		System.out.println("true");
//	}else{
//		System.out.println("false");
//	}
//
//	System.out.println(0l/2);
//	
	
	
	String[] strs=new String[]{"zhanggong",
			"yanaoshuang",
			"gaopeng",
			"jiangguiping",
			"yangweiguang",
			"zhushilong",
			"zhenghuanmin",
			"zhangjihong",
			"wujianmin",
			"zhangguanglian",
			"yemaolin",
			"zhanghongyu",
			"yangjinjing",
			"lichenggui",
			"guojiyong",
			"cuiyuqin",
			"xuanhong",
			"zhaoxinxin",
			"zhoulijun",
			"fangbinxing",
			"chengxu",
			"lijintao",
			"zhoushaoxiong",
			"xiejianxin",
			"sunfengchun",
			"liuguisheng",
			"fanweicheng",
			"chendanghui",
			"wangyongjun",
			"fanyubo",
			"raoyi",
			"renyongjie",
			"liuqingjun",
			"qujiuhui",
			"wuweihua",
			"sujiantong",
			"lidongru",
			"lijiulin",
			"gaochunhai",
			"yedahua",
			"lixiaohua",
			"zhangxianping",
			"zhouyan",
			"yuhong",
			"ganjing",
			"fengkeliang",
			"chentie",
			"cuixinwei",
			"chenzhongqiang",
			"xumingbo",
			"wuhanming",
			"luchangjun",
			"zhangshaoming",
			"nancewen",
			"luominmin",
			"duxiuli",
			"lixiaowei",
			"wenjianping",
			"luoyuliang",
			"dengxingwang"
};
	
	MD5 md=new MD5();
	
	for (String string : strs) {
		System.out.println(md.getMD5ofStr(string));
	}
	
	
}
}
