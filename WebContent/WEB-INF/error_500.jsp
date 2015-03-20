<%@ page contentType="text/html; charset=GBK" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>消息页</title>
<style type=text/css>
td {font-size:12px;line-height:150%;}
</style>
</head>
<body bgcolor="#e5f6fc">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <table width="482" border="0" align="center" cellpadding="0" cellspacing="0" background="${ctx}/commons/images/message_top.gif">
	  <tr>
	    <td height="109" background="${ctx}/commons/images/message_bottom.gif" style="padding-top:50px;background-repeat:no-repeat;background-position:bottom">
		    <table width="300" border="0" align="center" cellpadding="0" cellspacing="0">
		      <tr>
		        <td style="font-size:14px;font-weight:bold;">提示信息</td>
		      </tr>
		    </table>
		    <br>
			<table width="400px" border="0" cellspacing="0" cellpadding="0" align="center">
			  <tr>
			    <td>
			    <p>
			    			系统出错
					<br/>
			    </p>
			    <br/>
					<br/><br/>
					
			    </td>
			  </tr>
			</table>
		</td>
	  </tr>
	</table>
	</td></tr></table>
</body>
</html>
