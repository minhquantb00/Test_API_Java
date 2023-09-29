package hoadon.hoadon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hoadon.hoadon.repositorys.SanPhamRepo;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepo sanPhamRepo;

    public Boolean kiemTraSanPhamTonTai(Long id) {
        if (!sanPhamRepo.findById(id).isPresent()) {
            return false;
        }
        return true;
    }
}
