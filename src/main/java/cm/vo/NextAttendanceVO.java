package cm.vo;

import lombok.Data;

/**
 * 浏览器向服务器传送类型
 */
@Data
public class NextAttendanceVO {
    private String teamNumber;

    private String teamOrder;

    private String klassSeminarId;

    private String teamId;

    private Integer status;

    public void setTeamNumber(Integer klassSerial, Integer teamSerial) {
        this.teamNumber = String.valueOf(klassSerial) + "-" + String.valueOf(teamSerial);
    }
}
