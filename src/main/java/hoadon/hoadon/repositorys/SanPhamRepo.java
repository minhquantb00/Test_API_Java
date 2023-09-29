package hoadon.hoadon.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hoadon.hoadon.entity.SanPham;

@Repository
public interface SanPhamRepo extends JpaRepository<SanPham, Long> {
    public SanPham findByKyHieuSanPham(String kyHieuSanPham);
}
