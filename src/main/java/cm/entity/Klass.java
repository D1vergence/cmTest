package cm.entity;

import lombok.Data;
import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class Klass {
    private Long id;
    private Integer grade;
    private Byte klassSerial;
    private String klassTime;
    private String klassLocation;
    private List<Team>teams;
    private List<Student>students;

}
