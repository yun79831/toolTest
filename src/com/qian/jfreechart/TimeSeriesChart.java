package com.qian.jfreechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class TimeSeriesChart {
	/*
	 * 生成折线图
	 */
	ChartPanel frame1;
	JFreeChart jfreechart;
	/**
	 * 
	 * @param fileRealpath要保存图片的路径和名字
	 * @param title统计图的标题
	 * @param Xname统计图x轴的名称
	 * @param Yname统计图Y轴的名称
	 * @param map要显示的数据
	 * obj数组中存放数据的顺序为：年，月，数据量
	 */
	public TimeSeriesChart(String fileRealpath, String title, String Xname,
			String Yname,Map<String,List<Object[]>> map) {
		String filepath = fileRealpath.substring(0,
				fileRealpath.lastIndexOf("\\"));
		File dir = new File(filepath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		XYDataset xydataset = createDataset(map);
		jfreechart = ChartFactory.createTimeSeriesChart(title, Xname, Yname,
				xydataset, true, true, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(Color.white);//设置网格的背景颜色
		xyplot.setDomainGridlinePaint(Color.pink);//设置网格竖线颜色
		xyplot.setRangeGridlinePaint(Color.pink);//设置网格横线颜色
		xyplot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));// 设置曲线图与xy轴的距离
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
				   .getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);//设置曲线是否显示数据点
		 // 设置曲线显示各数据点的值--------------开始---------------
		 XYItemRenderer xyitem = xyplot.getRenderer();
		 xyitem.setBaseItemLabelsVisible(true);
		 xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(
		   ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		 xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		 xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 10));
		 xyplot.setRenderer(xyitem);
		 // 设置曲线显示各数据点的值--------------结束---------------
		 // 设置水平坐标轴--------------开始---------------
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy年MM月"));
		frame1 = new ChartPanel(jfreechart, true);
		dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
		dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
		dateaxis.setAutoRange(false);
		dateaxis.setAutoRangeMinimumSize(1);
		// 设置水平坐标轴--------------结束---------------
		// 设置垂直坐标轴--------------开始---------------
		ValueAxis rangeAxis = xyplot.getRangeAxis();// 获取柱状
		rangeAxis.setLowerMargin(10000);// 左边距 边框距离  
		rangeAxis.setUpperMargin(10000);// 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。  
        rangeAxis.setUpperMargin(10000);//上边距,防止最大的一个数据靠近了坐标轴。     
        rangeAxis.setAutoRange(false);   //不自动分配Y轴数据  
        rangeAxis.setTickMarkStroke(new BasicStroke(1.6f));     //设置线粗
        rangeAxis.setAxisLinePaint(Color.red);//设置曲线的颜色
        rangeAxis.setUpperBound(200f);//设置y轴最大值
        rangeAxis.setLowerBound(90f);//设置y轴最小值 
        rangeAxis.setTickMarkPaint(Color.red);     // 设置坐标标记颜色  
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		// 设置垂直坐标轴--------------结束---------------
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
		try {
			ChartUtilities.saveChartAsJPEG(new File(fileRealpath), jfreechart,
					1000, 500);
		} catch (IOException e) {
			System.out.println("生成图片出错");
		}

	}
	/**
	 * 
	 * @param map中list中的obj数组中存放数据的顺序为年，月，数据量
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static XYDataset createDataset(Map<String,List<Object[]>> map) { // 这个数据集有点多，但都不难理解
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		for(Entry<String,List<Object[]>> entry:map.entrySet()){
			String type=entry.getKey();
			TimeSeries timeSeries=new TimeSeries(type,Month.class);
			List<Object[]> list=entry.getValue();
			for(Object[] obj:list){
				String str=obj[0].toString();
				String[] date=str.split("-");
				timeSeries.add(new Month(Integer.parseInt(date[1])-1,Integer.parseInt(date[0])),Double.parseDouble(obj[1].toString()));
			}
			timeseriescollection.addSeries(timeSeries);
		}
		return timeseriescollection;
	}

	public ChartPanel getChartPanel() {
		return frame1;

	}

	public JFreeChart getJFreeChart() {
		return jfreechart;

	}

}
