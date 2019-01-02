package cm.vo;

import cm.entity.Student;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class KlassVO {
    private Integer grade;

    private Byte klassSerial;

    @JsonProperty("time")
    private String klassTime;

    @JsonProperty("location")
    private String klassLocation;

    @JsonProperty("id")
    private Long klassId;

    @JsonProperty("name")
    private String klassName;

    public void setKlassName(Integer grade, Byte klassSerial) {
        this.klassName = String.valueOf(grade)+"(" +String.valueOf(klassSerial)+")";
    }

    private List<UserVO> studentVOList;
}
