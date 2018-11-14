package springboot.wxcms.entity;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author hermit
 * @version 2.0
 * @date 2018-04-17 10:54:58
 */
@Data
@Entity
@Table(name = "sys_config")
public class SysConfig implements Serializable {

    @Id
    private String jkey;

    private String jvalue;

    private String description;
}
