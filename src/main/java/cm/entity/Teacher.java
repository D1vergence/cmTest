package cm.entity;

import lombok.Data;
import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class Teacher {
    private Long id;
    private String account;
    private String password;
    private String teacherName;
    private Byte isActive=0;
    private String email;

}
