package cm.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class AttendanceVO {

    private Long attendanceId;

    @JsonProperty("className")
    private String klassName;

    @JsonProperty("enrollOrder")
    private Byte teamOrder;

    private String teamName;

    private String reportName;

    private String pptName;

    private Byte isPresent;

    public void setKlassName(Integer grade, Byte klassSerial) {
        this.klassName = String.valueOf(grade)+"(" +String.valueOf(klassSerial)+")";
    }

    public void setTeamName(Byte klassSerial,Byte teamSerial){
        this.teamName=String.valueOf(klassSerial)+"-"+String.valueOf(teamSerial);
    }

    public void setTeamOrder(Integer teamOrder) {
        byte a= (byte) teamOrder.intValue();
        this.teamOrder=a;
    }
}
