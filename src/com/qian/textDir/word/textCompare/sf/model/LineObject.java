package com.qian.textDir.word.textCompare.sf.model;

import java.io.Serializable;
import java.util.Map;

/**
 * ת����ĵ������
 * @author Administrator
 *
 */
public class LineObject implements Serializable,java.lang.Cloneable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String str;//�����ַ���
	public Integer part;//�����
	public Integer lineNum;//�к�
	public Map<String, IntegerWrap> tf;//�ִ�
	//temp
	public String htmlTarget;//������ʾ��name
	public String rank;//��ʾ������ɫ��
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Integer getLineNum() {
		return lineNum;
	}
	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}
	public String getHtmlTarget() {
		return htmlTarget;
	}
	public void setHtmlTarget(String htmlTarget) {
		this.htmlTarget = htmlTarget;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Integer getPart() {
		return part;
	}
	public void setPart(Integer part) {
		this.part = part;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
		}

}
