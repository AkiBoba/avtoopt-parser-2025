package com.example.armtek_parcer_new.domain;

import java.util.List;
import java.util.Map;

public class BrandsResponse {
    public Map<String, BrandCategory> data;

    public static class BrandCategory {
        public int count;
        public List<BrandItem> items;
    }

    public static class BrandItem {
        public String ID;
        public String BCODE;
        public String BRAND;
        public String ALIAS;
    }
}
