package com.qian.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * ͼƬ���Ʋ�ѯ
�㷨������
���ȶ�Դͼ����Ҫɸѡ��ͼ�����ֱ��ͼ���ݲɼ����Բɼ��ĸ���ͼ��ֱ��ͼ���й�һ����
ʹ�ð���ϵ���㷨��ֱ��ͼ���ݽ��м��㣬���յó�ͼ�����ƶ�ֵ����ֵ��Χ��[0, 1]֮��
0��ʾ���䲻ͬ��1��ʾ�������ƣ���ͬ����
*/
public class Img2 {
	private int redBins;
	private int greenBins;
	private int blueBins;
	
	public Img2() {
		redBins = greenBins = blueBins = 4;
	}

	
	
	public static void main(String[] args) throws IOException {
		String  filename="C:/Users/Administrator/Desktop/SimilarImageSearch/images/source.jpg";
		String  filename1="C:/Users/Administrator/Desktop/SimilarImageSearch/images/example4.jpg";
		File inputFile = new File(filename); 
		BufferedImage sourceImage = ImageIO.read(inputFile);//��ȡͼƬ�ļ�  
		File inputFile1 = new File(filename1); 
		BufferedImage candidateImage = ImageIO.read(inputFile1);//��ȡͼƬ�ļ�  
		Img2 hfilter = new Img2();
		//ֱ��ͼ����
	
			float[] sourceData = hfilter.filter(sourceImage, null);
			float[] candidateData = hfilter.filter(candidateImage, null);
			double[] mixedData = new double[sourceData.length];
			//����ϵ��	
			for(int i=0; i<sourceData.length; i++ ) {
				mixedData[i] = Math.sqrt(sourceData[i] * candidateData[i]);
			}
			
			// The values of Bhattacharyya Coefficient ranges from 0 to 1,
			double similarity = 0;
			for(int i=0; i<mixedData.length; i++ ) {
				similarity += mixedData[i];
			}
			
			// The degree of similarity
			System.out.println("���ƶ�"+similarity);
	}
	
	
	
	
	

	public float[] filter(BufferedImage src, BufferedImage dest) {
		int width = src.getWidth();
        int height = src.getHeight();
        
        int[] inPixels = new int[width*height];
        float[] histogramData = new float[redBins * greenBins * blueBins];
        getRGB( src, 0, 0, width, height, inPixels );
        int index = 0;
        int redIdx = 0, greenIdx = 0, blueIdx = 0;
        int singleIndex = 0;
        float total = 0;
        for(int row=0; row<height; row++) {
        	int ta = 0, tr = 0, tg = 0, tb = 0;
        	for(int col=0; col<width; col++) {
        		index = row * width + col;
        		ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;
                redIdx = (int)getBinIndex(redBins, tr, 255);
                greenIdx = (int)getBinIndex(greenBins, tg, 255);
                blueIdx = (int)getBinIndex(blueBins, tb, 255);
                singleIndex = redIdx + greenIdx * redBins + blueIdx * redBins * greenBins;
                histogramData[singleIndex] += 1;
                total += 1;
        	}
        }
        
        // start to normalize the histogram data
        for (int i = 0; i < histogramData.length; i++)
        {
        	histogramData[i] = histogramData[i] / total;
        }
        
        return histogramData;
	}

	private float getBinIndex(int binCount, int color, int colorMaxValue) {
		float binIndex = (((float)color)/((float)colorMaxValue)) * ((float)binCount);
		if(binIndex >= binCount)
			binIndex = binCount  - 1;
		return binIndex;
	}
	
	public int[] getRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			return (int [])image.getRaster().getDataElements( x, y, width, height, pixels );
		return image.getRGB( x, y, width, height, pixels, 0, width );
    }
}
