package com.movebeans.lib.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huanghaibin
 * on 16-5-24.
 */
public class PageBean<T> implements Serializable {
    private List<T> items;
    private int page = 1;
    private int count = 10;
    private int total = 0;

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public int getTotal() {
        return total;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}