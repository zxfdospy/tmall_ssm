<%--
  Created by IntelliJ IDEA.
  User: zxfdo
  Date: 2019/2/17
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>

<%--<a href="${contextPath}">--%>
<%--<img id="logo" src="img/site/logo.gif" class="logo">--%>
<%--</a>--%>

<form action="foresearch" method="post">
    <div class="searchDiv-out">
        <a href="${contextPath}">
            <img id="logo" src="img/site/logo.gif" class="logo pull-left">
        </a>
        <div class="searchDiv pull-right">
            <input name="keyword" type="text" placeholder="时尚男鞋  太阳镜 ">
            <button type="submit" class="searchButton">搜索</button>
            <div class="searchBelow">
                <c:forEach items="${secs}" var="c" varStatus="st">
                    <c:if test="${st.count>=5 and st.count<=8}">
                        <span>
                            <a href="forecategory?cid=${c.id}">
                                    ${c.name}
                            </a>
                            <c:if test="${st.count!=8}">
                                <span>|</span>
                            </c:if>
                        </span>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
    <div style="clear: both"></div>
</form>
