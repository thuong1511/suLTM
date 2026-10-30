<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Không tìm thấy đơn hàng</title>
    <%@ include file="print-style.jspf" %>
</head>
<body>
<div class="sheet" style="text-align:center;">
    <h1>Không tìm thấy đơn hàng</h1>
    <p>Không có đơn hàng nào với mã "<b><c:out value="${ma}"/></b>".</p>
    <p><a href="${pageContext.request.contextPath}/print/orders">← Về danh sách đơn hàng</a></p>
</div>
</body>
</html>

