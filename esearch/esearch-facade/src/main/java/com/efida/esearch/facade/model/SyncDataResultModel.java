package com.efida.esearch.facade.model;

import java.io.Serializable;

public class SyncDataResultModel  implements Serializable {
    private static final long  serialVersionUID = 1L;

    private String dataId;
    private Boolean status;
    private String  errorMsg;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
