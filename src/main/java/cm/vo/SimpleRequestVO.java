package cm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class SimpleRequestVO {

    private Long requestId;
    private String courseName;
    private String subCourseName;
    private String subCourseTeacherName;

    @JsonProperty("className")
    private String klassName;
    private String leaderName;
    private String reason;
    /**
     * 处理状态
     * null为未处理
     * 0为不同意
     * 1为同意
     */
    private Byte status;

    public void setKlassName(Integer grade, Byte klassSerial) {
        this.klassName = String.valueOf(grade)+"(" +String.valueOf(klassSerial)+")";
    }
}
