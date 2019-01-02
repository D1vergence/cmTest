package cm.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class KlassSeminar {
    private Long id;
    private Long klassId;
    private Timestamp reportDdl;
    private Byte status=0;
    private Long seminarId;

}
