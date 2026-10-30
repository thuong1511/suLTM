<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hóa đơn ${hd.maDonHang} - EXED</title>
    <%@ include file="print-style.jspf" %>
</head>
<body>
<div class="toolbar">
    <button class="primary" onclick="window.print()">🖨 In hóa đơn</button>
    <a href="${pageContext.request.contextPath}/print/delivery/${hd.maDonHang}">Xem phiếu giao hàng</a>
    <a href="${pageContext.request.contextPath}/print/orders">Danh sách đơn</a>
</div>

<div class="sheet">
    <div class="head">
        <div class="brand">EXED<small>Limited Sneaker Collab</small></div>
        <div class="title">
            <h1>HÓA ĐƠN BÁN HÀNG</h1>
            <div>Số: <b><c:out value="${hd.maDonHang}"/></b></div>
            <div>Ngày lập: <c:out value="${hd.ngayTaoDon}"/></div>
        </div>
    </div>

    <div class="grid">
        <div class="box">
            <h3>Khách hàng</h3>
            <b><c:out value="${hd.hoTen}"/></b><br>
            ĐT: <c:out value="${hd.soDienThoai}"/><br>
            Địa chỉ: <c:out value="${hd.diaChi}"/>
        </div>
        <div class="box">
            <h3>Thông tin chiến dịch</h3>
            Chiến dịch: <b><c:out value="${hd.tenChienDich}"/></b><br>
            Nghệ sĩ: <c:out value="${hd.tenNgheSi}"/><br>
            Mã đăng ký: <c:out value="${hd.maDangKy}"/><br>
            Trạng thái giao: <c:out value="${hd.trangThaiGiaoHang}"/>
        </div>
    </div>

    <table>
        <thead>
        <tr>
            <th class="c" style="width:40px">#</th>
            <th>Sản phẩm</th>
            <th>Màu</th>
            <th class="c">Size</th>
            <th class="c">SL</th>
            <th class="r">Đơn giá</th>
            <th class="r">Thành tiền</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="it" items="${hd.items}" varStatus="s">
            <tr>
                <td class="c">${s.count}</td>
                <td><c:out value="${it.tenSanPham}"/></td>
                <td><c:out value="${it.mau}"/></td>
                <td class="c"><c:out value="${it.size}"/></td>
                <td class="c">${it.soLuong}</td>
                <td class="r"><c:out value="${it.donGia}"/></td>
                <td class="r"><c:out value="${it.thanhTien}"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty hd.items}">
            <tr><td colspan="7" class="c">Đơn hàng chưa có chi tiết sản phẩm</td></tr>
        </c:if>
        </tbody>
    </table>

    <table class="totals">
        <tr><td>Tổng số lượng</td><td class="r">${hd.tongSoLuong}</td></tr>
        <tr><td>Tổng giá trị hàng (giá chốt)</td><td class="r"><c:out value="${hd.tongHang}"/></td></tr>
        <tr><td>Đã thanh toán (<c:out value="${hd.phuongThuc}"/>)</td><td class="r"><c:out value="${hd.daThanhToan}"/></td></tr>
        <tr><td>Đã hoàn lại</td><td class="r">- <c:out value="${hd.hoanLai}"/></td></tr>
        <tr class="grand"><td>Thực trả</td><td class="r"><c:out value="${hd.thucTra}"/></td></tr>
    </table>
    <div style="font-size:13px;color:#666;">Ngày thanh toán: <c:out value="${hd.ngayThanhToan}"/></div>

    <div class="sign">
        <div>Người mua hàng<br><i>(Ký, ghi rõ họ tên)</i></div>
        <div>Người bán hàng<br><i>(Ký, ghi rõ họ tên)</i></div>
    </div>
    <div class="foot">Cảm ơn bạn đã đồng hành cùng EXED! · In lúc <c:out value="${hd.ngayIn}"/></div>
</div>
</body>
</html>

