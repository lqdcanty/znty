package com.efida.esearch.facade.constains;

public enum ESortable {
    EASC("asc"),EDESC("desc");
    private String sortType;

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    ESortable(String sortType){
        this.sortType=sortType;
    }
}
