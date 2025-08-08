package org.example.json;

import java.util.List;
import java.util.Objects;

public class Detail {
    private List<Good> goods;

    public Detail(List<Good> goods) {
        this.goods = goods;
    }
    private Detail(){

    }
    public List<Good> getGoods() {
        return goods;
    }

    @Override
    public String toString() {
        return "Detail[" +
               "goods=" + goods + ']';
    }
}
