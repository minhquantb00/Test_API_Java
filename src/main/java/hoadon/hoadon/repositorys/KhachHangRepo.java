package hoadon.hoadon.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hoadon.hoadon.entity.KhachHang;

@Repository
public interface KhachHangRepo extends JpaRepository<KhachHang, Long> {

}
