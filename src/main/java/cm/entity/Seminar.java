package cm.entity;

import lombok.Data;
import java.sql.Timestamp;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class Seminar {
    private Long id;
    private Long courseId;
    private Long roundId;
    private String seminarName;
    private String introduction;
    private Byte maxTeam;
    private Byte isVisible;
    private Byte seminarSerial;
    private Timestamp enrollStartTime;//报名
    private Timestamp enrollEndTime;

}
