package hoadon.hoadon.dto;

import java.time.LocalDate;
import java.util.List;

import hoadon.hoadon.entity.ChiTietHoaDon;
import hoadon.hoadon.entity.KhachHang;

public class HoaDonDto {
    public Long hoaDonId;
    public KhachHangDto khachHang;
    public List<ChiTietHoaDonDto> chiTietHoaDons;
}
