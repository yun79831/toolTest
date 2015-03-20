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
	 * ��������ͼ
	 */
	ChartPanel frame1;
	JFreeChart jfreechart;
	/**
	 * 
	 * @param fileRealpathҪ����ͼƬ��·��������
	 * @param titleͳ��ͼ�ı���
	 * @param Xnameͳ��ͼx�������
	 * @param Ynameͳ��ͼY�������
	 * @param mapҪ��ʾ������
	 * obj�����д�����ݵ�˳��Ϊ���꣬�£�������
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
		xyplot.setBackgroundPaint(Color.white);//��������ı�����ɫ
		xyplot.setDomainGridlinePaint(Color.pink);//��������������ɫ
		xyplot.setRangeGridlinePaint(Color.pink);//�������������ɫ
		xyplot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));// ��������ͼ��xy��ľ���
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
				   .getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);//���������Ƿ���ʾ���ݵ�
		 // ����������ʾ�����ݵ��ֵ--------------��ʼ---------------
		 XYItemRenderer xyitem = xyplot.getRenderer();
		 xyitem.setBaseItemLabelsVisible(true);
		 xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(
		   ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		 xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		 xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 10));
		 xyplot.setRenderer(xyitem);
		 // ����������ʾ�����ݵ��ֵ--------------����---------------
		 // ����ˮƽ������--------------��ʼ---------------
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy��MM��"));
		frame1 = new ChartPanel(jfreechart, true);
		dateaxis.setLabelFont(new Font("����", Font.BOLD, 14)); // ˮƽ�ײ�����
		dateaxis.setTickLabelFont(new Font("����", Font.BOLD, 12)); // ��ֱ����
		dateaxis.setAutoRange(false);
		dateaxis.setAutoRangeMinimumSize(1);
		// ����ˮƽ������--------------����---------------
		// ���ô�ֱ������--------------��ʼ---------------
		ValueAxis rangeAxis = xyplot.getRangeAxis();// ��ȡ��״
		rangeAxis.setLowerMargin(10000);// ��߾� �߿����  
		rangeAxis.setUpperMargin(10000);// �ұ߾� �߿����,��ֹ���ߵ�һ�����ݿ����������ᡣ  
        rangeAxis.setUpperMargin(10000);//�ϱ߾�,��ֹ����һ�����ݿ����������ᡣ     
        rangeAxis.setAutoRange(false);   //���Զ�����Y������  
        rangeAxis.setTickMarkStroke(new BasicStroke(1.6f));     //�����ߴ�
        rangeAxis.setAxisLinePaint(Color.red);//�������ߵ���ɫ
        rangeAxis.setUpperBound(200f);//����y�����ֵ
        rangeAxis.setLowerBound(90f);//����y����Сֵ 
        rangeAxis.setTickMarkPaint(Color.red);     // ������������ɫ  
		rangeAxis.setLabelFont(new Font("����", Font.BOLD, 15));
		// ���ô�ֱ������--------------����---------------
		jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("����", Font.BOLD, 20));// ���ñ�������
		try {
			ChartUtilities.saveChartAsJPEG(new File(fileRealpath), jfreechart,
					1000, 500);
		} catch (IOException e) {
			System.out.println("����ͼƬ����");
		}

	}
	/**
	 * 
	 * @param map��list�е�obj�����д�����ݵ�˳��Ϊ�꣬�£�������
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static XYDataset createDataset(Map<String,List<Object[]>> map) { // ������ݼ��е�࣬�����������
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
