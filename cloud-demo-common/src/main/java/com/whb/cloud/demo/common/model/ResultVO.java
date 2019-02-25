package com.whb.cloud.demo.common.model;

import java.io.Serializable;

public class ResultVO<T> implements Serializable {

    private static final String SUCCESS="000";
    private static final String DEFAULT_FAIL="999";
    /**
     * 页面需要的数据对象
     */
    private T data;
    /**
     * 业务处理结果  000 为成功  否则为失败 提示message
     */
    private String code;
    /**
     * 错误消息的提示信息
     */
    private String message;

    public ResultVO(){

    }

    private ResultVO(T data){
        this.data = data;
    }

    private ResultVO(T data,String code){
        this.data = data;
        this.code = code;
    }
    private ResultVO(T data,String code,String message){
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 成功返回对象
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> success(T data){
        return new ResultVO<T>(data,SUCCESS);
    }

    /**
     * 成功返回对象
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> success(){
        return new ResultVO(SUCCESS,SUCCESS);
    }

    /**
     * 失败返回错误消息
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> fail(String message){
        return fail(DEFAULT_FAIL,message);
    }

    public static <T> ResultVO<T> fail(String code,String message){
        return fail(null,code,message);
    }

    /**
     * 失败返回错误 数据 以及错误消息
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> fail(T data,String message){
        return fail(data,DEFAULT_FAIL,message);
    }

    /**
     * 失败返回自定义的code  错误数据 以及错误消息
     * @param data
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> fail(T data,String code,String message){
        return new ResultVO<T>(data,code,message);
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "data=" + data +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
