package springboot.wxcms.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import springboot.core.anno.NotToMap;
import springboot.core.anno.Phone;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sys_user")
@Data
public class SysUser extends BaseEntity {

    @NotBlank(message = "输入密码不能为空")
    private String password;

    private String loginName;
    @Email
    private String email;
    @Phone
    private String phone;
    private Date lastLoginDate;
    //性别 0男 1女
    private String sex;
    //姓名
    private String trueName;
    //更新时间
    private String updateTime;
    //状态
    private String flag;


    @JsonIgnore
    @Transient
    @NotToMap
    private List<Role> roleList;

    public List<String> getStringRoles() {
        List<String> list = new ArrayList<>();
        if (null != this.getRoleList()) {
            for (Role role : this.roleList) {
                list.add(role.getRoleName());
            }
        }
        return list;
    }

}