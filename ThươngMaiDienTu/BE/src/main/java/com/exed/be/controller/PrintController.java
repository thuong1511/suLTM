package com.exed.be.controller;

import com.exed.be.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Các trang JSP in hóa đơn / phiếu giao hàng (trả về view, không phải JSON).
 *   GET /print/orders                      → danh sách đơn để chọn in
 *   GET /print/invoice/{maDonHang}         → hóa đơn
 *   GET /print/delivery/{maDonHang}        → phiếu giao hàng
 */
@Controller
@RequestMapping("/print")
public class PrintController {

    @Autowired
    private PrintService printService;

    @GetMapping("/orders")
    public String danhSach(Model model) {
        model.addAttribute("orders", printService.danhSachDonHang());
        return "print-orders";
    }

    @GetMapping("/invoice/{maDonHang}")
    public String hoaDon(@PathVariable String maDonHang, Model model, HttpServletResponse res) {
        return printService.hoaDon(maDonHang)
                .map(hd -> {
                    model.addAttribute("hd", hd);
                    return "invoice";
                })
                .orElseGet(() -> notFound(maDonHang, model, res));
    }

    @GetMapping("/delivery/{maDonHang}")
    public String phieuGiao(@PathVariable String maDonHang, Model model, HttpServletResponse res) {
        return printService.phieuGiao(maDonHang)
                .map(pg -> {
                    model.addAttribute("pg", pg);
                    return "delivery";
                })
                .orElseGet(() -> notFound(maDonHang, model, res));
    }

    private String notFound(String ma, Model model, HttpServletResponse res) {
        res.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("ma", ma);
        return "print-notfound";
    }
}

