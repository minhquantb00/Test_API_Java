package hoadon.hoadon.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenLoaiSanPham;
    private Double giaThanh;
    private String moTa;
    private LocalDate nagyHetHan;
    private String kyHieuSanPham;

    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id")
    @JsonBackReference
    private LoaiSanPham loaiSanPham;

    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ChiTietHoaDon> chiTietHoaDons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenLoaiSanPham() {
        return tenLoaiSanPham;
    }

    public void setTenLoaiSanPham(String tenLoaiSanPham) {
        this.tenLoaiSanPham = tenLoaiSanPham;
    }

    public Double getGiaThanh() {
        return giaThanh;
    }

    public void setGiaThanh(Double giaThanh) {
        this.giaThanh = giaThanh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LocalDate getNagyHetHan() {
        return nagyHetHan;
    }

    public void setNagyHetHan(LocalDate nagyHetHan) {
        this.nagyHetHan = nagyHetHan;
    }

    public String getKyHieuSanPham() {
        return kyHieuSanPham;
    }

    public void setKyHieuSanPham(String kyHieuSanPham) {
        this.kyHieuSanPham = kyHieuSanPham;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public List<ChiTietHoaDon> getChiTietHoaDons() {
        return chiTietHoaDons;
    }

    public void setChiTietHoaDons(List<ChiTietHoaDon> chiTietHoaDons) {
        this.chiTietHoaDons = chiTietHoaDons;
    }

}
