package springboot.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户与角色对应关系表
 * 复合主键不能超过1000byte
 * 建立索引时，数据库计算key的长度是累加所有Index用到的字段的char长度后再按下面比例乘起来不能超过限定的key长度1000：
 * latin1 = 1 byte = 1 character
 * uft8 = 3 byte = 1 character
 * gbk = 2 byte = 1 character
 */
@Entity
@Table(name = "user_and_role")
public class UserAndRole implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(150)")
    private String userId;

    @Id
    @Column(columnDefinition = "varchar(150)")
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAndRole that = (UserAndRole) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return roleId != null ? roleId.equals(that.roleId) : that.roleId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}
