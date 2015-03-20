package com.qian.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// 将一个字符串按照zip方式压缩和解压缩
public class TestZIP {

  // 压缩
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

  // 解压缩
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
    // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
    return out.toString();
  }

  // 测试方法
  public static void main(String[] args) throws IOException {
	  String s=TestZIP.compress("桑德菲杰sd卡老费劲卡洛斯的缴费卡洛斯开朗大方sd卡龙卷风流口水就疯狂了的设计费圣诞节疯狂了就算了咖啡说多了a华盛顿顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶大啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");//压缩
	  System.out.println(s);
	  System.out.println(TestZIP.uncompress(s));//解压输出
	  System.out.println(s.length()+"----"+TestZIP.uncompress(s).length());
  }

}