package com.exed.be.service;

import com.exed.be.model.ChiTietDonHang;
import com.exed.be.model.ChienDich;
import com.exed.be.model.DangKyChienDich;
import com.exed.be.model.DonHang;
import com.exed.be.model.PhieuGiaoHang;
import com.exed.be.model.ThanhToan;
import com.exed.be.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Chuẩn bị dữ liệu (đã định dạng sẵn) cho các trang JSP in hóa đơn / phiếu giao hàng.
 * Dữ liệu trả về là Map để JSP đọc trực tiếp bằng EL, ví dụ ${hd.maDonHang}.
 */
@Service
public class PrintService {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat VND = NumberFormat.getInstance(Locale.of("vi", "VN"));

    @Autowired
    private DonHangRepository donHangRepository;

    /** Danh sách đơn hàng (trang chọn đơn để in). */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> danhSachDonHang() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DonHang dh : donHangRepository.findAll()) {
            DangKyChienDich dk = dh.getDangKyChienDich();
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("maDonHang", dh.getMaDonHang());
            m.put("tenChienDich", dk != null && dk.getChienDich() != null ? dk.getChienDich().getTenChienDich() : "—");
            m.put("nguoiNhan", dk != null && dk.getThanhToan() != null ? dk.getThanhToan().getHoTenNguoiNhan() : "—");
            m.put("trangThai", dh.getTrangThaiGiaoHang());
            m.put("ngayTaoDon", fmt(dh.getNgayTaoDon()));
            m.put("coPhieuGiao", dh.getPhieuGiaoHang() != null);
            list.add(m);
        }
        return list;
    }

    /** Dữ liệu hóa đơn. Trả rỗng nếu không tìm thấy đơn. */
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> hoaDon(String maDonHang) {
        return donHangRepository.findById(maDonHang).map(dh -> {
            Map<String, Object> hd = base(dh);

            DangKyChienDich dk = dh.getDangKyChienDich();
            ThanhToan tt = dk != null ? dk.getThanhToan() : null;

            BigDecimal donGia = nz(dh.getGiaChotCuoiCung());
            int tongSl = 0;
            List<Map<String, Object>> items = new ArrayList<>();
            for (ChiTietDonHang ct : safe(dh.getChiTietDonHangs())) {
                int sl = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                tongSl += sl;
                Map<String, Object> it = item(dh, ct);
                it.put("donGia", money(donGia));
                it.put("thanhTien", money(donGia.multiply(BigDecimal.valueOf(sl))));
                items.add(it);
            }
            BigDecimal tongHang = donGia.multiply(BigDecimal.valueOf(tongSl));
            BigDecimal daThanhToan = tt != null ? nz(tt.getSoTienThanhToan()) : BigDecimal.ZERO;
            BigDecimal hoanLai = Boolean.TRUE.equals(dh.getDaHoanTien()) ? nz(dh.getSoTienHoanLai()) : BigDecimal.ZERO;

            hd.put("items", items);
            hd.put("tongSoLuong", tongSl);
            hd.put("tongHang", money(tongHang));
            hd.put("daThanhToan", money(daThanhToan));
            hd.put("hoanLai", money(hoanLai));
            hd.put("thucTra", money(daThanhToan.subtract(hoanLai)));
            hd.put("phuongThuc", tt != null ? tt.getPhuongThuc() : "—");
            hd.put("ngayThanhToan", tt != null ? fmt(tt.getNgayThanhToan()) : "—");
            return hd;
        });
    }

    /** Dữ liệu phiếu giao hàng. Trả rỗng nếu không tìm thấy đơn. */
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> phieuGiao(String maDonHang) {
        return donHangRepository.findById(maDonHang).map(dh -> {
            Map<String, Object> pg = base(dh);

            List<Map<String, Object>> items = new ArrayList<>();
            int tongSl = 0;
            for (ChiTietDonHang ct : safe(dh.getChiTietDonHangs())) {
                tongSl += ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                items.add(item(dh, ct));
            }
            pg.put("items", items);
            pg.put("tongSoLuong", tongSl);

            PhieuGiaoHang p = dh.getPhieuGiaoHang();
            pg.put("coPhieu", p != null);
            if (p != null) {
                pg.put("maVanDon", p.getMaVanDon());
                pg.put("donViVanChuyen", p.getDonViVanChuyen());
                pg.put("ngayDangKy", fmt(p.getNgayDangKy()));
                pg.put("ngayChuanBi", fmt(p.getNgayChuanBi()));
                pg.put("ngayGiao", fmt(p.getNgayGiao()));
                pg.put("ngayNhan", fmt(p.getNgayNhan()));
                pg.put("ghiChuGiao", p.getGhiChu() != null ? p.getGhiChu() : "");
            }
            return pg;
        });
    }

    // ----------------------------------------------------------------- helpers

    private Map<String, Object> base(DonHang dh) {
        DangKyChienDich dk = dh.getDangKyChienDich();
        ThanhToan tt = dk != null ? dk.getThanhToan() : null;
        ChienDich cd = dk != null ? dk.getChienDich() : null;

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("maDonHang", dh.getMaDonHang());
        m.put("ngayTaoDon", fmt(dh.getNgayTaoDon()));
        m.put("trangThaiGiaoHang", dh.getTrangThaiGiaoHang());
        m.put("maDangKy", dk != null ? dk.getMaDangKy() : "—");
        m.put("tenChienDich", cd != null ? cd.getTenChienDich() : "—");
        m.put("tenNgheSi", cd != null && cd.getNgheSi() != null ? cd.getNgheSi().getTenNgheSi() : "—");
        m.put("hoTen", tt != null ? tt.getHoTenNguoiNhan() : "—");
        m.put("soDienThoai", tt != null ? tt.getSoDienThoaiNhan() : "—");
        m.put("diaChi", tt != null ? tt.getDiaChiGiaoHang() : "—");
        m.put("ngayIn", fmt(LocalDateTime.now()));
        return m;
    }

    private Map<String, Object> item(DonHang dh, ChiTietDonHang ct) {
        DangKyChienDich dk = dh.getDangKyChienDich();
        ChienDich cd = dk != null ? dk.getChienDich() : null;
        String ten = cd != null && cd.getSanPham() != null ? cd.getSanPham().getTenSanPham() : null;
        if (ten == null && cd != null) ten = cd.getTenChienDich();

        Map<String, Object> it = new LinkedHashMap<>();
        it.put("tenSanPham", ten != null ? ten : "—");
        it.put("mau", ct.getMauSac() != null ? ct.getMauSac().getTenMau() : "—");
        it.put("size", ct.getKichThuoc() != null ? ct.getKichThuoc().getTenSize() : "—");
        it.put("soLuong", ct.getSoLuong());
        return it;
    }

    private static <T> List<T> safe(List<T> l) {
        return l != null ? l : new ArrayList<>();
    }

    private static BigDecimal nz(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private static String money(BigDecimal v) {
        return VND.format(v) + " đ";
    }

    private static String fmt(LocalDateTime t) {
        return t != null ? t.format(DATE_TIME) : "—";
    }
}

