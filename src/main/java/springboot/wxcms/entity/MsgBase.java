package springboot.wxcms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class MsgBase extends BaseEntity {
    private String msgtype;//消息类型;
    private String inputcode;//关注者发送的消息
    @JsonIgnore
    private String rule;//规则，目前是 “相等”
    @JsonIgnore
    private Integer enable;//是否可用
    @JsonIgnore
    private Integer readcount;//消息阅读数
    @JsonIgnore
    private Integer favourcount;//消息点赞数

}
