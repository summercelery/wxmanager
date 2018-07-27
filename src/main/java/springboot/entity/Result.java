package springboot.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 返回数据
 *
 * @param <T>
 */
public class Result<T> {

    //服务器返回数据
    private T date;

    //是否成功
    private Boolean success;

    //错误信息
    private String message;

    //响应时间
    private Date dateTime;

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.dateTime = new Date();
    }

    public Result(T t) {
        this.success = true;
        this.date = t;
        this.dateTime = new Date();
    }

    public static Result ok() {
        return new Result(true, "ok");
    }

    public static <T> Result ok(T t) {
        return new <T>Result(t);
    }

    public static Result ok(String message) {
        return new Result(true, message);
    }

    public static Result fail(String message) {
        return new Result(false, message);
    }


    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimetemp() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        return sdf.format(this.dateTime);
    }

    public void setTimetemp(Date dateTime) {
        this.dateTime = dateTime;
    }


}

