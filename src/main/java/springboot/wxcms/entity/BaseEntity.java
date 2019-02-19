package springboot.wxcms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import springboot.core.util.UUIDUtil;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

//jpa用来标志父类的注解
@MappedSuperclass
@Data
public abstract class BaseEntity extends Page{

    BaseEntity() {
        this.createTime = LocalDateTime.now();
    }

    @Id
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    //创建时间
    private LocalDateTime createTime;
}
