package springboot.wxcms.entity;

import lombok.Data;
import springboot.core.constant.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 返回数据
 *
 * @param <T>
 */
@Data
public class Result<T> {

    //服务器返回数据
    private T date;

    //是否成功
    private Boolean success;

    //错误信息
    private String msg;

    //响应时间
    private Date dateTime;

    //分页信息
    private Page page;

    public Result(Boolean success, String message) {
        this.success = success;
        this.msg = message;
        this.dateTime = new Date();
    }

    public Result(T t) {
        this.success = true;
        this.date = t;
        this.msg =Constants.MSG_SUCCESS;
        this.dateTime = new Date();
    }

    public static Result ok() {
        return new Result(true, Constants.MSG_SUCCESS);
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

    public String getTimetemp() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        return sdf.format(this.dateTime);
    }

}

