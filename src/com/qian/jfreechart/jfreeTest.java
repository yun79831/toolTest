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
		//getInitPie();//��ʼ������ȡ��״ͼ
		getBar();
	}


	public static void getInitPie() {
		String savePath="D:\\ ��Ŀ״̬�ֲ�.jpg";
		String title="��Ŀ���ȷֲ�1";
		Map<String, Double> map=new HashMap<String, Double>();
		map.put(" �г�ǰ��d", new Double(10));
		map.put(" ����", new Double(15));
		map.put(" �ƻ�", new Double(10));
		map.put(" ���������", new Double(200));
		map.put(" ִ�п���", new Double(35));
		map.put(" ��β", new Double(10));
		map.put(" ��ά", new Double(10));
		 getPie(title,savePath,map);
	}
	
	
	public static void getBar() {
		String savePath="D:\\ ��Ŀ״̬�ֲ�.jpg";
		String title="��Ŀ���ȷֲ�1";
		double[][] data = new double[][] {{1230,1110,1120,1210}, {720,750,860,800}, {830,780,790,700,}, {400,380,390,450}};
		String[] rowKeys = {"ƻ��", "�㽶", "����", "����"};
		String[] columnKeys = {"�ױ�","����","����","����"};
		getBar(title, "����", "ˮ��", savePath, data, rowKeys, columnKeys);
	}

	
	/**
	 * ��״ͼ
	 * @param title
	 * @param savePath
	 * @param map
	 */
	public static void getPie(String title,String savePath,Map<String, Double> map ) {
		//Ҫ�������ݶ���
		DefaultPieDataset dataset = new DefaultPieDataset();
		//�������
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
			// ʹ��˵����ǩ��������,ȥ��������ڵ�Ч��   
			chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,   
			    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);   
			
			// �����������ķ���
		    TextTitle textTitle = chart.getTitle();
			textTitle.setFont(new Font("����", Font.PLAIN, 20));
			//���������˵ײ��������������
			chart.getLegend().setItemFont(new Font("����", Font.PLAIN, 12));
			
		  PiePlot3D  plot=(PiePlot3D)chart.getPlot();  
		    // ͼƬ����ʾ�ٷֱ�:Ĭ�Ϸ�ʽ  
		    //plot.setLabelGenerator(new           StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));  
		// ͼƬ����ʾ�ٷֱ�:�Զ��巽ʽ��{0} ��ʾѡ� {1} ��ʾ��ֵ�� {2} ��ʾ��ռ���� ,С�������λ  
		 plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));   
		// ͼ����ʾ�ٷֱ�:�Զ��巽ʽ�� {0} ��ʾѡ� {1} ��ʾ��ֵ�� {2} ��ʾ��ռ����                  
		 plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})"));   
		// ���ñ���ɫΪ��ɫ   
		chart.setBackgroundPaint(Color.white);   
		// ָ��ͼƬ��͸����(0.0-1.0)   
		// plot.setForegroundAlpha(1.0f);   
		// ָ����ʾ�ı�ͼ��Բ��(false)����Բ��(true)   
		plot.setCircular(true);   
		  // ����ͼ�Ϸ����ǩlabel�����壬�����������
		plot.setLabelFont(new Font("����", Font.PLAIN, 12));
		// ����ͼ���������   
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
	 * ��״ͼ
	 * @param title
	 * @param savePath
	 * @param map
	 */
	public static void getBar(String bigTitle,String XTitle,String YTitle,String savePath,	double[][] data,String[] rowKeys, String[] columnKeys){
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); 
				//�Ƿ���ʾͼ��
				boolean tl=true;
				//���ǵ�ָ���ʱ����ʾͼ��
			
		JFreeChart chart = ChartFactory.createBarChart3D(
				bigTitle,// ͼ�����
				XTitle , // Ŀ¼�����ʾ��ǩ
				YTitle,  // ��ֵ�����ʾ��ǩ
				dataset, // ���ݼ�
				PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ--PlotOrientation.HORIZONTALˮƽ
				true, // �Ƿ���ʾͼ��
				true, // �Ƿ����ɹ���
				false);// �Ƿ����� URL ����
		
		
		// �����������ķ���
	    TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("����", Font.PLAIN, 20));
		
		//���������˵ײ��������������
		if(tl){
			chart.getLegend().setItemFont(new Font("����", Font.PLAIN, 12));
		}

		CategoryPlot plot = chart.getCategoryPlot(); 
	
		//�������񱳾���ɫ 
		plot.setBackgroundPaint(Color.white); 
		//��������������ɫ 
		plot.setDomainGridlinePaint(Color.pink); 
		//�������������ɫ 
		plot.setRangeGridlinePaint(Color.pink); 
		
		//��ȡy�����
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis(); 
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
		//�������������Ƶ�����,ͬʱ�����������
		numberaxis.setLabelFont(new Font("����", Font.PLAIN, 12)); 
		//��������������ʾ���������� 
		numberaxis.setTickLabelFont(new Font("Fixedsys",Font.PLAIN,12)); 
		
		//numberaxis.setTickUnit(new NumberTickUnit(5D)); // y�ᵥλ���Ϊ0.1  
		
		//��ȡx�����
		org.jfree.chart.axis.CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickLabelFont(new Font("����", Font.PLAIN, 12));//������������������
		domainAxis.setLabelFont(new Font("����", Font.PLAIN, 12));//�����¶˵ı������壬ͬʱ�����������
	    domainAxis.setLowerMargin(0.05);//���þ���ͼƬ��˾���
	    domainAxis.setUpperMargin(0.05);//���þ���ͼƬ�Ҷ˾���
	    domainAxis.setCategoryLabelPositionOffset(10);//ͼ��������ǩ�ľ���(10����)
	    domainAxis.setCategoryMargin(0.2);//�����ǩ֮��ľ���20%
	    
	    //x�������������ʾ������ʱ���ô˷���
	    if(true){
	    	domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
	    }

		//��ʾÿ��������ֵ�����޸ĸ���ֵ���������� 
		BarRenderer3D renderer = new BarRenderer3D(); 
		renderer.setItemLabelFont(new Font("Fixedsys",Font.PLAIN,10));// ÿ��������ʾ�����ֵ�����
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
		renderer.setBaseItemLabelsVisible(true); 

		//Ĭ�ϵ�������ʾ�������У�ͨ����������ɵ������ֵ���ʾ 
		//ע�⣺�˾�ܹؼ������޴˾䣬�����ֵ���ʾ�ᱻ���ǣ���������û����ʾ���������� 
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT)); 
		renderer.setItemLabelAnchorOffset(10D); 
	
		plot.setRenderer(renderer); 
	
		//���·�����ʾ�ŵ��ҷ� 
		//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT); 
		
		//��Ĭ�Ϸ�����ߵĸ����ŵ��ҷ� 
		//plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT); 
	
		//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
		//��Ĭ�Ϸ�����ߵġ��������ŵ��ҷ�
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
	 * ����ͼ
	 * @param title
	 * @param savePath
	 * @param map
	 */
}
