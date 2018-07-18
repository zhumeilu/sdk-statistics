package com.yishen.sdk.query;

public class QueryObject {

    private int pageSize = 10;
    private int pageNo = 1;

    public int getStart(){
        return (pageNo-1)*pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
