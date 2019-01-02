package cm.vo;
import cm.entity.KlassSeminarReportDDL;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class SeminarModifyVO {

    private String courseName;

    private String seminarName;

    private String introduction;

    private Byte isVisible;

    private String enrollStartTime;

    private String enrollEndTime;

    private Long seminarId;

    @JsonProperty("classId")
    private Long klassId;

    private Byte teamNumLimit;

    private Byte roundSerial;

    private List<KlassSeminarReportDDL> reportDDLList;

}
