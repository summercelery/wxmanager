package springboot.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import springboot.anno.NotToMap;
import springboot.anno.Phone;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    public User(){
        super();
        this.isDelete = false;
        this.createDate = new Date();
    }



    @NotBlank(message = "输入密码不能为空")
    private String password;

    private String name;

    @Email
    private String email;

    @Phone
    private String phone;

    @JsonIgnore
    @Transient
    @NotToMap
    private List<Role> roleList;

    private Date createDate;

    private Date lastLoginDate;

    private Boolean isDelete;

    public List<String> getStringRoles() {
        List<String> list = new ArrayList<>();
        if (null != this.getRoleList()) {
            for (Role role : this.roleList) {
                list.add(role.getRoleName());
            }
        }
        return list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }


    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}