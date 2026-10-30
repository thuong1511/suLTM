package com.exed.be.repository;

import com.exed.be.model.DangKyChienDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DangKyChienDichRepository extends JpaRepository<DangKyChienDich, Integer> {
    List<DangKyChienDich> findByNguoiDung_MaNguoiDung(String maNguoiDung);
    List<DangKyChienDich> findByChienDich_MaChienDich(String maChienDich);
    Optional<DangKyChienDich> findByThanhToan_MaThanhToan(Integer maThanhToan);

    // Query toi uu cho danh sach don hang va dashboard Admin (JOIN FETCH tranh N+1 queries)
    @Query("SELECT DISTINCT dk FROM DangKyChienDich dk " +
           "LEFT JOIN FETCH dk.thanhToan " +
           "LEFT JOIN FETCH dk.nguoiDung " +
           "LEFT JOIN FETCH dk.chienDich cd " +
           "LEFT JOIN FETCH dk.donHang dh " +
           "LEFT JOIN FETCH dh.phieuGiaoHang " +
           "ORDER BY dk.ngayDangKy DESC")
    List<DangKyChienDich> findAllOptimizedForAdmin();
}