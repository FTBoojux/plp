package org.example.utils;

import java.util.Collections;
import java.util.List;

public final class Bytes {
    private Bytes(){}

    public static byte[] toArray(List<Byte> byteList) {
        if (CollectionUtils.isEmpty(byteList)) {
            return new byte[0];
        } else {
            byte[] byteArray = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                byteArray[i] = byteList.get(i);
            }
            return byteArray;
        }
    }
}
