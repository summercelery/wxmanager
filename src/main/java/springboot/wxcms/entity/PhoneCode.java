package springboot.wxcms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import springboot.core.anno.Phone;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Entity
@Table(name = "phone_code")
@Data
public class  PhoneCode extends BaseEntity {

    public PhoneCode() {
        super();
        this.state = "create";
    }

    @JsonIgnore
    //登陆类型:login 注册类型:register
    private String type;

    private String message;

    @Phone
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @JsonIgnore
    @NotBlank(message = "验证码不能为空")
    private String code;

    @JsonIgnore
    private Date clockTime;

    private String state;

    @JsonIgnore
    private Date sendTime;

    private Date expireTime;


}