package cm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KlassSeminarReportDDL {

    @JsonProperty("className")
    private String klassName;

    private Long klassId;

    private Long seminarId;

    private String reportDDL;

    public void setKlassName(Integer grade, Byte klassSerial) {
        this.klassName = String.valueOf(grade)+"(" +String.valueOf(klassSerial)+")";
    }
}
