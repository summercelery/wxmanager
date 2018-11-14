package springboot.wxcms.entity;

import lombok.Data;

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
@Data
public class UserAndRole implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(150)")
    private String userId;

    @Id
    @Column(columnDefinition = "varchar(150)")
    private String roleId;
}
