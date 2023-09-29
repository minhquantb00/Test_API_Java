package hoadon.hoadon.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hoadon.hoadon.entity.ChiTietHoaDon;

@Repository
public interface ChiTietHoaDonRepo extends JpaRepository<ChiTietHoaDon, Long> {
    @Query(value = "select * from chi_tiet_hoa_don where hoa_don_id = :id", nativeQuery = true)
    public List<ChiTietHoaDon> getHoaDonTheoId(Long id);
}
