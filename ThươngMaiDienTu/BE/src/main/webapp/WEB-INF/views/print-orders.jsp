<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chọn đơn hàng để in - EXED</title>
    <%@ include file="print-style.jspf" %>
</head>
<body>
<div class="sheet">
    <div class="head">
        <div class="brand">EXED<small>In hóa đơn / phiếu giao hàng</small></div>
        <div class="title"><h1>DANH SÁCH ĐƠN HÀNG</h1></div>
    </div>
    <table>
        <thead>
        <tr><th>Mã đơn</th><th>Chiến dịch</th><th>Người nhận</th><th>Ngày tạo</th><th>Trạng thái</th><th class="c">In</th></tr>
        </thead>
        <tbody>
        <c:forEach var="o" items="${orders}">
            <tr>
                <td><b><c:out value="${o.maDonHang}"/></b></td>
                <td><c:out value="${o.tenChienDich}"/></td>
                <td><c:out value="${o.nguoiNhan}"/></td>
                <td><c:out value="${o.ngayTaoDon}"/></td>
                <td><c:out value="${o.trangThai}"/></td>
                <td class="c">
                    <a href="${pageContext.request.contextPath}/print/invoice/${o.maDonHang}" target="_blank">Hóa đơn</a> ·
                    <a href="${pageContext.request.contextPath}/print/delivery/${o.maDonHang}" target="_blank">Phiếu giao</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty orders}">
            <tr><td colspan="6" class="c">Chưa có đơn hàng nào</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>

