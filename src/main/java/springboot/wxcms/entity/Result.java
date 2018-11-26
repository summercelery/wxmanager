package springboot.wxcms.entity;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import springboot.core.constant.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 返回数据
 *
 * @param <T>
 */
@Data
public class Result<T> {

    //服务器返回数据
    private T data;

    //是否成功
    private Boolean success;

    //错误信息
    private String msg;

    //响应时间
    private LocalDateTime dateTime;

    //分页信息
    private springboot.wxcms.entity.Page page;

    public Result(Boolean success, String message) {
        this.success = success;
        this.msg = message;
        this.dateTime = LocalDateTime.now();
    }


    public Result(T t) {
        if(t instanceof Page){
            Page newPage = (Page) t;
            springboot.wxcms.entity.Page  p = new springboot.wxcms.entity.Page();
            p.setPageNum(newPage.getPageNum());
            p.setPageSize(newPage.getPageSize());
            p.setPages(newPage.getPages());
            p.setTotal(newPage.getTotal());
            page = p;
        }
        this.success = true;
        this.data = t;
        this.msg =Constants.MSG_SUCCESS;
        this.dateTime = LocalDateTime.now();
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
        return this.dateTime.format( DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS"));
    }

}

