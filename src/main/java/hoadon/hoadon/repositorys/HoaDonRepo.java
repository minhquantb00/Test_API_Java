package hoadon.hoadon.repositorys;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hoadon.hoadon.entity.HoaDon;

@Repository
public interface HoaDonRepo extends JpaRepository<HoaDon, Long> {
        public HoaDon findByMaGiaoDich(String MaGiaoDich);

        public List<HoaDon> findAllByOrderByThoiGianTaoDesc(Pageable pageable);

        //select * from hoa_don where thoi_gian_tao > fromDate between thoi_gian_tao <  toDate order by thoi_gian_tao limit cái j đấy 
        public List<HoaDon> findAllByThoiGianTaoBetweenOrderByThoiGianTaoDesc(LocalDate fromDate, LocalDate toDate,
                        Pageable pageable);
        //

        public List<HoaDon> findAllByTongTienBetweenOrderByThoiGianTaoDesc(Double fromPrice, Double toPrice,
                        Pageable pageable);

        //
        public List<HoaDon> findAllByMaGiaoDichOrderByThoiGianTaoDesc(String maGiaoDich, Pageable pageable);
}
