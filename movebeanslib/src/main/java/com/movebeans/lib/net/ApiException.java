package com.movebeans.lib.net;

/**
 * ClassName: ApiException
 * Description: 该类属于自定义接口异常，code ！= 0；
 * Creator: chenwei
 * Date: 16/8/27 17:14
 * Version: 1.0
 */
public class ApiException extends RuntimeException {


    private int resultCode;
    private String errorMsg;

    /**
     * @param resultCode 错误码
     * @param errorMsg   异常信息
     */
    public ApiException(int resultCode, String errorMsg) {
        this.resultCode = resultCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getResultCode() {
        return resultCode;
    }
}
