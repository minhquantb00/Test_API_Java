package hoadon.hoadon.services;

import java.lang.StackWalker.Option;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hoadon.hoadon.dto.ChiTietHoaDonDto;
import hoadon.hoadon.dto.HoaDonDto;
import hoadon.hoadon.dto.ResHoaDonDto;
import hoadon.hoadon.entity.ChiTietHoaDon;
import hoadon.hoadon.entity.HoaDon;
import hoadon.hoadon.entity.KhachHang;
import hoadon.hoadon.entity.SanPham;
import hoadon.hoadon.helpers.TaoMaGiaoDich;
import hoadon.hoadon.repositorys.HoaDonRepo;
import hoadon.hoadon.repositorys.KhachHangRepo;
import hoadon.hoadon.repositorys.SanPhamRepo;
import hoadon.hoadon.repositorys.ChiTietHoaDonRepo;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepo hoaDonRepo;
    @Autowired
    private KhachHangRepo khachHangRepo;
    @Autowired
    private SanPhamRepo sanPhamRepo;
    @Autowired
    private ChiTietHoaDonRepo ChiTietHoaDonRepo;
    @Autowired
    private SanPhamService sanPhamService;

    public List<HoaDon> findAllByOrderByThoiGianTaoDesc(Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("thoiGianTao").descending());
        return hoaDonRepo.findAllByOrderByThoiGianTaoDesc(paging);
    }

    public List<HoaDon> findAllByMaGiaoDichOrTenKhachHangOrderByThoiGianTaoDesc(String maGiaoDich, String tenKhachHang,
            Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("thoiGianTao").descending());
        return hoaDonRepo.findAllByMaGiaoDichOrderByThoiGianTaoDesc("20230413_39x", paging);
    }

    public List<HoaDon> findAllByThoiGianTaoBetweenOrderByThoiGianTaoDesc(LocalDate fromDate, LocalDate toDate,
            Integer page, Integer size) {
        // Pageable paging = PageRequest.of(page, size,
        // Sort.by("thoiGianTao").descending());
        Pageable paging = PageRequest.of(page, size, Sort.by("thoiGianTao").descending());
        return hoaDonRepo.findAllByThoiGianTaoBetweenOrderByThoiGianTaoDesc(fromDate, toDate, paging);
    }

    public List<HoaDon> findAllByTongTienBetweenOrderByThoiGianTaoDesc(Double fromPrice, Double toPrice, Integer page,
            Integer size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("thoiGianTao").descending());
        return hoaDonRepo.findAllByTongTienBetweenOrderByThoiGianTaoDesc(fromPrice, toPrice, paging);
    }

    public ResHoaDonDto themMoiBanGhi(HoaDonDto hoaDonDto) {
        Optional<KhachHang> khachHangGetOne = khachHangRepo.findById(hoaDonDto.khachHang.id);
        String maHoaDonString = TaoMaGiaoDich.NgayHomNay(hoaDonRepo.count());
        // kiểm tra người dùng đã tồn tại hay chưa
        if (!khachHangGetOne.isPresent()) {
            return null;
        }
        // kiểm tra sản phẩm đã tồn tại hay chưa
        for (ChiTietHoaDonDto hTietHoaDonDto : hoaDonDto.chiTietHoaDons) {
            if (!sanPhamService.kiemTraSanPhamTonTai(hTietHoaDonDto.sanPhamId)) {
                return null;
            }
        }
        HoaDon hoaDonNew = new HoaDon();
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        // thêm vào danh sách sản phẩm
        hoaDonNew.setMaGiaoDich(maHoaDonString);
        hoaDonNew.setKhachHang(khachHangGetOne.get());
        hoaDonNew.setThoiGianTao(LocalDate.now());
        hoaDonNew.setTongTien(0.0);

        hoaDonRepo.save(hoaDonNew);

        System.out.println(hoaDonNew.getId());
        // thêm vào chi tiết
        for (ChiTietHoaDonDto ctHoaDon : hoaDonDto.chiTietHoaDons) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            SanPham sanPham = sanPhamRepo.findById(ctHoaDon.sanPhamId).get();
            ct.setDonViTinh(ctHoaDon.donViTinh);
            ct.setHoaDon(hoaDonNew);
            ct.setSanPham(sanPham);
            ct.setSoLuong(ctHoaDon.soLuong);
            ct.setThanhTien(sanPham.getGiaThanh() * ctHoaDon.soLuong);
            chiTietHoaDons.add(ct);
        }
        System.out.println(chiTietHoaDons.get(0).getDonViTinh());

        hoaDonNew.setTongTien(chiTietHoaDons.stream().mapToDouble(x -> x.getThanhTien()).sum());
        hoaDonRepo.save(hoaDonNew);
        hoaDonNew.setKhachHang(khachHangGetOne.get());
        khachHangRepo.save(khachHangGetOne.get());
        hoaDonNew.setChiTietHoaDons(chiTietHoaDons);
        HoaDon HD = hoaDonNew;
        HD.setKhachHang(khachHangGetOne.get());
        ChiTietHoaDonRepo.saveAll(chiTietHoaDons);
        ResHoaDonDto res = new ResHoaDonDto();
        res.khachHang = khachHangGetOne.get();
        res.hoaDon = hoaDonNew;
        return res;
    }

    public ResHoaDonDto suaHoaDon(HoaDonDto hoaDonDto) {

        // kiểm tra id hóa đơn đó đã tồn tại hay chưa
        Optional<HoaDon> hoaDonUpdate = hoaDonRepo.findById(hoaDonDto.hoaDonId);
        if (!hoaDonUpdate.isPresent()) {
            return null;
        }
        Optional<KhachHang> khachHangGetOne = khachHangRepo.findById(hoaDonDto.khachHang.id);
        String maHoaDonString = TaoMaGiaoDich.NgayHomNay(hoaDonRepo.count());
        // kiểm tra người dùng đã tồn tại hay chưa
        if (!khachHangGetOne.isPresent()) {
            return null;
        }
        // kiểm tra sản phẩm đã tồn tại hay chưa
        List<ChiTietHoaDon> ChiTietHoaDonUpdate = new ArrayList<>();
        HoaDon hoaDon = hoaDonUpdate.get();
        hoaDon.setThoiGianCapNhat(LocalDate.now());
        hoaDonRepo.save(hoaDon);
        List<ChiTietHoaDon> hTietHoaDons = ChiTietHoaDonRepo.getHoaDonTheoId(hoaDonDto.hoaDonId);
        ChiTietHoaDonRepo.deleteAll(hTietHoaDons);
        for (ChiTietHoaDonDto hTietHoaDonDto : hoaDonDto.chiTietHoaDons) {
            if (!sanPhamService.kiemTraSanPhamTonTai(hTietHoaDonDto.sanPhamId)) {
                return null;
            } else {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                SanPham sanPham = sanPhamRepo.findById(hTietHoaDonDto.sanPhamId).get();
                ct.setDonViTinh(hTietHoaDonDto.donViTinh);
                ct.setHoaDon(hoaDon);
                ct.setSanPham(sanPham);
                ct.setSoLuong(hTietHoaDonDto.soLuong);
                ct.setThanhTien(sanPham.getGiaThanh() * hTietHoaDonDto.soLuong);
                ChiTietHoaDonUpdate.add(ct);
            }
        }
        ChiTietHoaDonRepo.saveAll(ChiTietHoaDonUpdate);
        hoaDon.setChiTietHoaDons(ChiTietHoaDonUpdate);
        System.out.println(hTietHoaDons.size());
        ResHoaDonDto res = new ResHoaDonDto();
        res.khachHang = khachHangGetOne.get();
        res.hoaDon = hoaDon;
        return res;
    }

    public Boolean XoaHoaDon(Long idHoaDon) {
        Optional<HoaDon> hoaDonUpdate = hoaDonRepo.findById(idHoaDon);
        if (!hoaDonUpdate.isPresent()) {
            return false;
        }
        List<ChiTietHoaDon> hTietHoaDons = ChiTietHoaDonRepo.getHoaDonTheoId(idHoaDon);
        ChiTietHoaDonRepo.deleteAll(hTietHoaDons);

        hoaDonRepo.delete(hoaDonUpdate.get());
        return true;

    }
}
