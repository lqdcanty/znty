package com.efida.esearch.model;

public class AppQueryTplDetailKey {
    private String appId;

    private String docTplCode;

    private String queryTplCode;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getDocTplCode() {
        return docTplCode;
    }

    public void setDocTplCode(String docTplCode) {
        this.docTplCode = docTplCode == null ? null : docTplCode.trim();
    }

    public String getQueryTplCode() {
        return queryTplCode;
    }

    public void setQueryTplCode(String queryTplCode) {
        this.queryTplCode = queryTplCode == null ? null : queryTplCode.trim();
    }
}