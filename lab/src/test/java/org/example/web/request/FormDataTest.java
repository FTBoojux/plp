package org.example.web.request;

import framework.FtAssert;
import org.example.utils.CollectionUtils;
import org.example.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormDataTest {
    public static void main(String[] args) {
        FormData formData = new FormData();
        formData.put("name","Boojux");
        formData.put("sex","male");
        formData.put("age",Integer.parseInt("1"));
        formData.put("height", 172.3);
        formData.put("categories","meat");
        formData.put("categories","fish");
        Map<String, String> item1 = new HashMap<>();
        item1.put("address","address1");
        formData.put("items",item1);
        Map<String,String> item2 = new HashMap<>();
        item2.put("address","address2");
        formData.put("items",item2);

        String name = formData.getString("name");
        FtAssert.fAssert(StringUtils.equals(name, "Boojux"));

        String sex = formData.getString("sex");
        FtAssert.fAssert(StringUtils.equals(sex, "male"));

        Integer age = formData.getInteger("age");
        FtAssert.fAssert(age == 1);

        double height = (double)formData.getNumber("height");
        FtAssert.fAssert(height==172.3);

        List<String> categories = formData.getAll("categories", String.class);
        FtAssert.fAssert(!CollectionUtils.isEmpty(categories));

        List<Item> items = formData.getAll("items", Item.class);
        FtAssert.fAssert(!CollectionUtils.isEmpty(items));
    }
    static class Item{
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
