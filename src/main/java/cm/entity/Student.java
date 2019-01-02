package cm.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class Student {
    private Long id;
    private String account;
    private String password;
    private Byte isActive=0;
    private String studentName;
    private String email;
    List<Long>courseIdList;

}
