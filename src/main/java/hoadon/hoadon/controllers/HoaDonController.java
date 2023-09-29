package hoadon.hoadon.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hoadon.hoadon.ResData.ResListData;
import hoadon.hoadon.ResData.ResOneData;
import hoadon.hoadon.dto.HoaDonDto;
import hoadon.hoadon.dto.ResHoaDonDto;
import hoadon.hoadon.entity.HoaDon;
import hoadon.hoadon.services.HoaDonService;

@RestController
@RequestMapping("/api/hoa-don")
public class HoaDonController {

    @Autowired
    public HoaDonService hoaDonService;

    // lấy danh sách hóa đơn sắp xếp theo thời gian tạo mới nhất
    @GetMapping(value = "get")
    public ResponseEntity<ResListData<HoaDon>> get(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        List<HoaDon> data = hoaDonService.findAllByOrderByThoiGianTaoDesc(page, size);
        return new ResponseEntity<>(new ResListData<HoaDon>(data, "đã trả về thành công dữ liệu", 200, page, size),
                HttpStatus.OK);
    }

    // lấy danh sách hóa đơn theo năm, tháng
    @GetMapping(value = "get/{year}/{month}")
    public ResponseEntity<ResListData<HoaDon>> get1(@PathVariable int year, @PathVariable int month,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        LocalDate fromDate = LocalDate.of(year, month, 1);
        LocalDate toDate = fromDate.plusMonths(1).minusDays(1); // LẤY NGÀY CUỐI CÙNG CỦA THÁNG TRUYỀN VÀO 1/4  30/4
        List<HoaDon> Data = hoaDonService.findAllByThoiGianTaoBetweenOrderByThoiGianTaoDesc(fromDate, toDate, page,
                size);
        return new ResponseEntity<ResListData<HoaDon>>(
                new ResListData<>(Data, "đã lấy dữ liệu thành công", 200, page, size), HttpStatus.OK);
    }

    // Lấy hóa đơn được tạo từ ngày ... đến ngày
    @GetMapping(value = "get/from")
    public ResponseEntity<ResListData<HoaDon>> get2(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {

        List<HoaDon> Data = hoaDonService.findAllByThoiGianTaoBetweenOrderByThoiGianTaoDesc(fromDate,
                toDate, page, size);
        return new ResponseEntity<ResListData<HoaDon>>(
                new ResListData<HoaDon>(Data, "bạn đã hiển thị thành công", 200, page, size), HttpStatus.OK);
    }

    // Lấy hóa đơn theo tổng tiền từ XXXX -> XXXX
    @GetMapping(value = "get/from/tong-tien")
    public List<HoaDon> get3(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "fromPrice", defaultValue = "0") Double fromPrice,
            @RequestParam(value = "toPrice", defaultValue = "10") Double toPrice) {

        return hoaDonService.findAllByTongTienBetweenOrderByThoiGianTaoDesc(fromPrice,
                toPrice, page, size);
    }

    // lấy danh sách hóa đơn theo mã giao dịch hoặc tên hóa đơn
    @GetMapping(value = "get/search")
    public List<HoaDon> get4(@RequestParam(required = false, defaultValue = "") String maGiaoDich,
            @RequestParam(required = false, defaultValue = "") String tenKhachHang,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        List<HoaDon> Data = hoaDonService.findAllByMaGiaoDichOrTenKhachHangOrderByThoiGianTaoDesc(maGiaoDich,
                tenKhachHang, page,
                size);
        return Data;
    }

    @PostMapping(value = "post")
    public ResponseEntity<ResOneData<ResHoaDonDto>> post(@RequestBody HoaDonDto hoaDonDto) {

        ResHoaDonDto hoaDonCreate = hoaDonService.themMoiBanGhi(hoaDonDto);
        if (hoaDonCreate.hoaDon == null) {
            return new ResponseEntity<>(new ResOneData<ResHoaDonDto>(null, "thêm thất bại"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResOneData<ResHoaDonDto>(hoaDonCreate, "bạn đã thêm thành công"),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "put")
    public ResponseEntity<ResOneData<ResHoaDonDto>> Put(@RequestBody HoaDonDto hoaDonDto) {
        ResHoaDonDto hoaDonUpdate = hoaDonService.suaHoaDon(hoaDonDto);
        if (hoaDonUpdate == null) {
            return new ResponseEntity<>(new ResOneData<ResHoaDonDto>(hoaDonUpdate, "sửa thất bại thất bại"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResOneData<ResHoaDonDto>(hoaDonUpdate, "bạn đã sửa thành công"),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "del/{id}")
    public ResponseEntity<String> dels(@PathVariable Long id) {
        if (hoaDonService.XoaHoaDon(id)) {
            return new ResponseEntity<>("bạn đã xóa thành công", HttpStatus.OK);
        }
        return new ResponseEntity<>("bạn đã xóa thất bại", HttpStatus.BAD_GATEWAY);
    }
}
