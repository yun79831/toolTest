package com.qian.textDir.word.textCompare.sf.model;

import java.io.Serializable;
//�ִ�ʵ����
public class IntegerWrap implements Serializable{

	public int value;
	
	public IntegerWrap(){
		value=0;
	}
	
	public static IntegerWrap newOne(){
		return new IntegerWrap();
	}
}
