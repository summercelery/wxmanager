package springboot.wxcms.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "email")
@Data
public class Email extends BaseEntity{

    public Email(){
        super();
        this.createDate = new Date();
        this.state = "create";
    }

    private String sendAddress;

    private String receiveAddress;

    private String title;

    private String content;

    private String state;

    private Date sendDate;

    private Date createDate;
}
