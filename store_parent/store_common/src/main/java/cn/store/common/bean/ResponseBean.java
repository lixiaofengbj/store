package cn.store.common.bean;


import cn.store.common.constant.ErrorEnum;
import cn.store.common.utils.CommonUtils;

import java.io.Serializable;

public class ResponseBean implements Serializable {


    private static final long serialVersionUID = 862017689923854902L;

    private Integer errorCode = ErrorEnum.SUCCESS.getErrorCode();

    private String errorMessage = ErrorEnum.SUCCESS.getErrorMessage();

    private Object data;

    private ErrorEnum errorEnum;

    public ResponseBean(Object data) {
        this.data = data;
    }

    public ResponseBean(Integer errorCode, String errorMessage, Object data) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public ResponseBean(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ResponseBean() {
    }

    public ResponseBean(ErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }
    public ResponseBean(ErrorEnum errorEnum, Object data) {
        this.errorEnum = errorEnum;
        this.data = data;
    }

    public Integer getErrorCode() {
        if (CommonUtils.isNotNull(this.errorEnum)) {
            return this.errorEnum.getErrorCode();
        }
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        if (CommonUtils.isNotNull(this.errorEnum)) {
            return this.errorEnum.getErrorMessage();
        }
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
