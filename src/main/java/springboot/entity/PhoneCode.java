package springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import springboot.anno.NotToMap;
import springboot.anno.Phone;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Entity
@Table(name = "phone_code")
public class PhoneCode extends BaseEntity {

    public PhoneCode() {
        super();
        this.createTime = new Date();
        this.state = "create";
    }

    @NotToMap
    @JsonIgnore
    //登陆类型:login 注册类型:register
    private String type;

    private String message;

    @Phone
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotToMap
    @JsonIgnore
    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotToMap
    @JsonIgnore
    private Date clockTime;

    @NotToMap
    @JsonIgnore
    private Date createTime;

    @NotToMap
    @JsonIgnore
    private String state;

    @NotToMap
    @JsonIgnore
    private Date sendTime;

    private Date expireTime;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSendTime() {
        return sendTime;
    }


    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getClockTime() {
        return clockTime;
    }

    public void setClockTime(Date clockTime) {
        this.clockTime = clockTime;
    }
}