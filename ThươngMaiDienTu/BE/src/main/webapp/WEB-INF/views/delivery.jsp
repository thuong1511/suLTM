<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Phiếu giao hàng ${pg.maDonHang} - EXED</title>
    <%@ include file="print-style.jspf" %>
</head>
<body>
<div class="toolbar">
    <button class="primary" onclick="window.print()">🖨 In phiếu giao hàng</button>
    <a href="${pageContext.request.contextPath}/print/invoice/${pg.maDonHang}">Xem hóa đơn</a>
    <a href="${pageContext.request.contextPath}/print/orders">Danh sách đơn</a>
</div>

<div class="sheet">
    <div class="head">
        <div class="brand">EXED<small>Limited Sneaker Collab</small></div>
        <div class="title">
            <h1>PHIẾU GIAO HÀNG</h1>
            <div>Đơn hàng: <b><c:out value="${pg.maDonHang}"/></b></div>
            <div>Ngày in: <c:out value="${pg.ngayIn}"/></div>
        </div>
    </div>

    <c:choose>
        <c:when test="${pg.coPhieu}">
            <p><span class="barcode"><c:out value="${pg.maVanDon}"/></span></p>
            <div class="grid">
                <div class="box">
                    <h3>Người nhận</h3>
                    <b><c:out value="${pg.hoTen}"/></b><br>
                    ĐT: <c:out value="${pg.soDienThoai}"/><br>
                    Địa chỉ: <c:out value="${pg.diaChi}"/>
                </div>
                <div class="box">
                    <h3>Vận chuyển</h3>
                    Mã vận đơn: <b><c:out value="${pg.maVanDon}"/></b><br>
                    Đơn vị: <c:out value="${pg.donViVanChuyen}"/><br>
                    Chiến dịch: <c:out value="${pg.tenChienDich}"/><br>
                    Trạng thái: <b><c:out value="${pg.trangThaiGiaoHang}"/></b>
                </div>
            </div>

            <ul class="timeline">
                <li class="done">Đăng ký chiến dịch — <c:out value="${pg.ngayDangKy}"/></li>
                <li class="${pg.ngayChuanBi != '—' ? 'done' : ''}">Chuẩn bị hàng (chiến dịch kết thúc) — <c:out value="${pg.ngayChuanBi}"/></li>
                <li class="${pg.ngayGiao != '—' ? 'done' : ''}">Bàn giao vận chuyển — <c:out value="${pg.ngayGiao}"/></li>
                <li class="${pg.ngayNhan != '—' ? 'done' : ''}">Người nhận đã nhận hàng — <c:out value="${pg.ngayNhan}"/></li>
            </ul>
        </c:when>
        <c:otherwise>
            <div class="box" style="margin-bottom:18px;">
                <h3>Người nhận</h3>
                <b><c:out value="${pg.hoTen}"/></b> · <c:out value="${pg.soDienThoai}"/><br>
                <c:out value="${pg.diaChi}"/><br>
                <i style="color:#c62828;">Đơn hàng này chưa có phiếu giao hàng (chưa có mã vận đơn).</i>
            </div>
        </c:otherwise>
    </c:choose>

    <table>
        <thead>
        <tr>
            <th class="c" style="width:40px">#</th>
            <th>Sản phẩm</th>
            <th>Màu</th>
            <th class="c">Size</th>
            <th class="c">SL</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="it" items="${pg.items}" varStatus="s">
            <tr>
                <td class="c">${s.count}</td>
                <td><c:out value="${it.tenSanPham}"/></td>
                <td><c:out value="${it.mau}"/></td>
                <td class="c"><c:out value="${it.size}"/></td>
                <td class="c">${it.soLuong}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty pg.items}">
            <tr><td colspan="5" class="c">Chưa có chi tiết sản phẩm</td></tr>
        </c:if>
        </tbody>
    </table>
    <div style="font-size:14px;"><b>Tổng số lượng: ${pg.tongSoLuong}</b></div>
    <c:if test="${not empty pg.ghiChuGiao}">
        <div style="font-size:13px;color:#555;margin-top:6px;">Ghi chú: <c:out value="${pg.ghiChuGiao}"/></div>
    </c:if>

    <div class="sign">
        <div>Người giao hàng<br><i>(Ký, ghi rõ họ tên)</i></div>
        <div>Người nhận hàng<br><i>(Ký, ghi rõ họ tên)</i></div>
    </div>
    <div class="foot">Vui lòng kiểm tra hàng trước khi ký nhận · EXED</div>
</div>
</body>
</html>

