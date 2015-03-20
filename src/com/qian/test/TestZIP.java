package com.qian.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// ��һ���ַ�������zip��ʽѹ���ͽ�ѹ��
public class TestZIP {

  // ѹ��
  public static String compress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(out);
    gzip.write(str.getBytes());
    gzip.close();
    return out.toString("ISO-8859-1");
  }

  // ��ѹ��
  public static String uncompress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayInputStream in = new ByteArrayInputStream(str
        .getBytes("ISO-8859-1"));
    GZIPInputStream gunzip = new GZIPInputStream(in);
    byte[] buffer = new byte[256];
    int n;
    while ((n = gunzip.read(buffer)) >= 0) {
      out.write(buffer, 0, n);
    }
    // toString()ʹ��ƽ̨Ĭ�ϱ��룬Ҳ������ʽ��ָ����toString("GBK")
    return out.toString();
  }

  // ���Է���
  public static void main(String[] args) throws IOException {
	  String s=TestZIP.compress("ɣ�·ƽ�sd���ϷѾ�����˹�Ľɷѿ���˹���ʴ�sd�����������ˮ�ͷ���˵���Ʒ�ʥ���ڷ���˾����˿���˵����a��ʢ�ٶ����������������������������������������������������������������������󰡰�����������������������������������");//ѹ��
	  System.out.println(s);
	  System.out.println(TestZIP.uncompress(s));//��ѹ���
	  System.out.println(s.length()+"----"+TestZIP.uncompress(s).length());
  }

}