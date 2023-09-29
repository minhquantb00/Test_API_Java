package hoadon.hoadon.ResData;

import java.security.PublicKey;
import java.util.List;

public class ResListData<T> {
    public List<T> Data;
    public String mess;
    public Integer status;
    public Integer page;
    public Integer size;
    public ResListData(List<T> data, String mess, Integer status, Integer page, Integer size) {
        Data = data;
        this.mess = mess;
        this.status = status;
        this.page = page;
        this.size = size;
    }

}
