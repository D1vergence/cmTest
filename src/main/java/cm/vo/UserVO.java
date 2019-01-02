package cm.vo;

import lombok.Data;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/21
 */
@Data
public class UserVO {

    private Long id;
    private String account;
    private String role;
    private String name;
    private String email;
    private Byte isActive;

}
