package cm.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/19
 */
@Data
public class SeminarScore {
    private BigDecimal totalScore;
    private BigDecimal presentationScore=new BigDecimal(0);
    private BigDecimal questionScore=new BigDecimal(0);
    private BigDecimal reportScore=new BigDecimal(0);

    public void setTotalScore(Byte presentationPercentage,Byte questionPercentage,Byte reportPercentage){
        Double d1=presentationScore.doubleValue()*presentationPercentage/100,
                d2=questionScore.doubleValue()*questionPercentage/100,
                d3=reportScore.doubleValue()*reportPercentage/100;
        totalScore=(new BigDecimal(d1)).add(new BigDecimal(d2)).add(new BigDecimal(d3));
        totalScore=totalScore.setScale(1,BigDecimal.ROUND_HALF_UP);
    }
}
