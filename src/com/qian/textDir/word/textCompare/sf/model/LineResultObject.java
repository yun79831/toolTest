package com.qian.textDir.word.textCompare.sf.model;

import java.io.Serializable;
/**
 * 对比结果集合
 * @author 
 *
 */
public class LineResultObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double similarity;//相似度
	public String line1;//文章1相似语句
	public String line2;//文章2相似语句
	public Integer leftLineNum;//文章1行号
	public Integer rightLineNum;//文章2行号
	
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
		return "对象 [similarity=" + similarity + ", line1="
				+ line1 + ", line2=" + line2 + ", leftLineNum=" + leftLineNum
				+ ", rightLineNum=" + rightLineNum + "]\r\n\r\n\r\n\r\n";
	}
	
}
