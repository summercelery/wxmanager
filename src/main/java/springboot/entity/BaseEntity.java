package springboot.entity;

import springboot.util.UUIDUtil;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

//jpa用来标志父类的注解
@MappedSuperclass
public abstract class BaseEntity implements Serializable{

    BaseEntity() {
        this.id = UUIDUtil.getUUID();
    }

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

}
