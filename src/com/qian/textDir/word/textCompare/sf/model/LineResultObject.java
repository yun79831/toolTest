package com.qian.textDir.word.textCompare.sf.model;

import java.io.Serializable;
/**
 * �ԱȽ������
 * @author 
 *
 */
public class LineResultObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double similarity;//���ƶ�
	public String line1;//����1�������
	public String line2;//����2�������
	public Integer leftLineNum;//����1�к�
	public Integer rightLineNum;//����2�к�
	
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public Integer getLeftLineNum() {
		return leftLineNum;
	}
	public void setLeftLineNum(Integer leftLineNum) {
		this.leftLineNum = leftLineNum;
	}
	public Integer getRightLineNum() {
		return rightLineNum;
	}
	public void setRightLineNum(Integer rightLineNum) {
		this.rightLineNum = rightLineNum;
	}
	@Override
	public String toString() {
		return "���� [similarity=" + similarity + ", line1="
				+ line1 + ", line2=" + line2 + ", leftLineNum=" + leftLineNum
				+ ", rightLineNum=" + rightLineNum + "]\r\n\r\n\r\n\r\n";
	}
	
}
