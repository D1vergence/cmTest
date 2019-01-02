package cm.entity;


import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class Course {
    private Long id;
    private String courseName;
    private String introduction;
    private Byte presentationPercentage;
    private Byte questionPercentage;
    private Byte reportPercentage;
    private Timestamp teamStartTime;
    private Timestamp teamEndTime;
    private Long teamMainCourseId;
    private Long seminarMainCourseId;
    private List<Round>rounds;
    private List<Klass>klasses;

}
