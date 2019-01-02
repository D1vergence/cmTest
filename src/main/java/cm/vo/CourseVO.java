package cm.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class CourseVO {

    private Long id;
    private String name;
    private Boolean isShareTeam;
    private Boolean isShareSeminar;

    List<RoundVO> roundVOList;
}
