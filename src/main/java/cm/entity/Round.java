package cm.entity;


import lombok.Data;
import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class Round {
    private Long id;
    private Byte roundSerial;
    private Byte presentationScoreMethod;
    private Byte reportScoreMethod;
    private Byte questionScoreMethod;
    private List<Seminar>seminars;

}
