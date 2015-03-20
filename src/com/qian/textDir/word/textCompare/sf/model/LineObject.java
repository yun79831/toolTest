package com.qian.textDir.word.textCompare.sf.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 转换后的单句对象
 * @author Administrator
 *
 */
public class LineObject implements Serializable,java.lang.Cloneable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String str;//单句字符串
	public Integer part;//段落号
	public Integer lineNum;//行号
	public Map<String, IntegerWrap> tf;//分词
	//temp
	public String htmlTarget;//关联显示的name
	public String rank;//显示级别（颜色）
	
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
