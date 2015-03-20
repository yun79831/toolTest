package com.qian.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
public class jfreeTest {

	public static void main(String[] args) {
		//getInitPie();//初始化并获取饼状图
		getBar();
	}


	public static void getInitPie() {
		String savePath="D:\\ 项目状态分布.jpg";
		String title="项目进度分布1";
		Map<String, Double> map=new HashMap<String, Double>();
		map.put(" 市场前期d", new Double(10));
		map.put(" 立项", new Double(15));
		map.put(" 计划", new Double(10));
		map.put(" 需求与设计", new Double(200));
		map.put(" 执行控制", new Double(35));
		map.put(" 收尾", new Double(10));
		map.put(" 运维", new Double(10));
		 getPie(title,savePath,map);
	}
	
	
	public static void getBar() {
		String savePath="D:\\ 项目状态分布.jpg";
		String title="项目进度分布1";
		double[][] data = new double[][] {{1230,1110,1120,1210}, {720,750,860,800}, {830,780,790,700,}, {400,380,390,450}};
		String[] rowKeys = {"苹果", "香蕉", "橘子", "梨子"};
		String[] columnKeys = {"鹤壁","西安","深圳","北京"};
		getBar(title, "销量", "水果", savePath, data, rowKeys, columnKeys);
	}

	
	/**
	 * 饼状图
	 * @param title
	 * @param savePath
	 * @param map
	 */
	public static void getPie(String title,String savePath,Map<String, Double> map ) {
		//要填充的数据对象
		DefaultPieDataset dataset = new DefaultPieDataset();
		//填充数据
		for (String key : map.keySet()) {
			dataset.setValue(key, map.get(key));
		}
		
		 JFreeChart chart = ChartFactory.createPieChart3D(  
		    title, // chart title  
		    dataset,// data  
		    true,// include legend  
		    true,  
		    false  
		   );  
			// 使下说明标签字体清晰,去锯齿类似于的效果   
			chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,   
			    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);   
			
			// 解决标题乱码的方案
		    TextTitle textTitle = chart.getTitle();
			textTitle.setFont(new Font("黑体", Font.PLAIN, 20));
			//这句代码解决了底部汉字乱码的问题
			chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
			
		  PiePlot3D  plot=(PiePlot3D)chart.getPlot();  
		    // 图片中显示百分比:默认方式  
		    //plot.setLabelGenerator(new           StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));  
		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位  
		 plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));   
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例                  
		 plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})"));   
		// 设置背景色为白色   
		chart.setBackgroundPaint(Color.white);   
		// 指定图片的透明度(0.0-1.0)   
		// plot.setForegroundAlpha(1.0f);   
		// 指定显示的饼图上圆形(false)还椭圆形(true)   
		plot.setCircular(true);   
		  // 设置图上分类标签label的字体，解决中文乱码
		plot.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		// 设置图标题的字体   
		FileOutputStream fos_jpg = null;   
		try {   
		     fos_jpg=new FileOutputStream(savePath);   
		     ChartUtilities.writeChartAsJPEG(fos_jpg,1.0f,chart,640,480,null);   
		     fos_jpg.close();   
		} catch (Exception e) {   
			System.out.println(e.toString());
		 }
	}
	
	/**
	 * 柱状图
	 * @param title
	 * @param savePath
	 * @param map
	 */
	public static void getBar(String bigTitle,String XTitle,String YTitle,String savePath,	double[][] data,String[] rowKeys, String[] columnKeys){
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); 
				//是否显示图例
				boolean tl=true;
				//当是单指标的时候不显示图例
			
		JFreeChart chart = ChartFactory.createBarChart3D(
				bigTitle,// 图表标题
				XTitle , // 目录轴的显示标签
				YTitle,  // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直--PlotOrientation.HORIZONTAL水平
				true, // 是否显示图例
				true, // 是否生成工具
				false);// 是否生成 URL 链接
		
		
		// 解决标题乱码的方案
	    TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		
		//这句代码解决了底部汉字乱码的问题
		if(tl){
			chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
		}

		CategoryPlot plot = chart.getCategoryPlot(); 
	
		//设置网格背景颜色 
		plot.setBackgroundPaint(Color.white); 
		//设置网格竖线颜色 
		plot.setDomainGridlinePaint(Color.pink); 
		//设置网格横线颜色 
		plot.setRangeGridlinePaint(Color.pink); 
		
		//获取y轴操作
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis(); 
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
		//设置纵坐标名称的字体,同时解决乱码问题
		numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12)); 
		//设置纵坐标上显示的数字字体 
		numberaxis.setTickLabelFont(new Font("Fixedsys",Font.PLAIN,12)); 
		
		//numberaxis.setTickUnit(new NumberTickUnit(5D)); // y轴单位间隔为0.1  
		
		//获取x轴操作
		org.jfree.chart.axis.CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 12));//横坐标中文乱码问题
		domainAxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));//设置下端的标题字体，同时解决乱码问题
	    domainAxis.setLowerMargin(0.05);//设置距离图片左端距离
	    domainAxis.setUpperMargin(0.05);//设置距离图片右端距离
	    domainAxis.setCategoryLabelPositionOffset(10);//图表横轴与标签的距离(10像素)
	    domainAxis.setCategoryMargin(0.2);//横轴标签之间的距离20%
	    
	    //x轴下面的文字显示不开的时候用此方法
	    if(true){
	    	domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
	    }

		//显示每个柱的数值，并修改该数值的字体属性 
		BarRenderer3D renderer = new BarRenderer3D(); 
		renderer.setItemLabelFont(new Font("Fixedsys",Font.PLAIN,10));// 每个柱上显示的数字的字体
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
		renderer.setBaseItemLabelsVisible(true); 

		//默认的数字显示在柱子中，通过如下两句可调整数字的显示 
		//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题 
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT)); 
		renderer.setItemLabelAnchorOffset(10D); 
	
		plot.setRenderer(renderer); 
	
		//将下方的显示放到右方 
		//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT); 
		
		//将默认放在左边的个数放到右方 
		//plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT); 
	
		//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
		//将默认放在左边的“销量”放到右方
		//plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		FileOutputStream fos_jpg = null;   
		try {   
		     fos_jpg=new FileOutputStream(savePath);   
		     ChartUtilities.writeChartAsJPEG(fos_jpg,1.0f,chart,640,480,null);   
		     fos_jpg.close();   
		} catch (Exception e) {   
			System.out.println(e.toString());
		 }
		
	}
	
	
	/**
	 * 折线图
	 * @param title
	 * @param savePath
	 * @param map
	 */
}
