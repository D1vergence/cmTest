package cm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class CourseKlassVO {

    private Long courseId;

    private String courseName;

    @JsonProperty("classId")
    private Long klassId;

    @JsonProperty("className")
    private String klassName;

    public void setKlassName(Integer grade, Byte klassSerial) {
        this.klassName = String.valueOf(grade)+"(" +String.valueOf(klassSerial)+")";
    }


}
