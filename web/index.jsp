<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/7/6
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath %>"/>
    <script language="JavaScript" src="js/jquery-3.0.0.min.js"/>
    <title></title>
</head>
<body>
<a : href="/getAllEvent">Get All Event</a>


</body>
<script>
    $.get(
            "http://soulsing.imwork.net/WeiBoSpider/getBeiJingEvent.action",
//          {Id: id},
            function (data) {
                alert();//数据传回来的时候怎么做
            },
            "text");
</script>
<script>
    var params = {
        type : "安全",
        content : "测试",
        time : "2016-7-10",
    }
    $.post(
            "http://soulsing.imwork.net/WeiBoSpider/addNewEvent.action",
            params,
            function (data) {
                alert(data);//数据传回来的时候怎么做
            },
            "text");
    $.get(
            "http://soulsing.imwork.net/WeiBoSpider/deleteEvent.action",
            {id:8990},
            function(data){
                alert("delete Success");
            }
    )
</script>

</html>
