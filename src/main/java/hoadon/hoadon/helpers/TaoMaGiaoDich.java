package hoadon.hoadon.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaoMaGiaoDich {
    public static String NgayHomNay(Long sobanGhi) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + (sobanGhi + 1) + "x";
    }
}
