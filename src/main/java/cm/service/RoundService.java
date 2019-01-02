package cm.service;

import cm.dao.*;
import cm.entity.*;
import cm.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoundService {
    @Autowired
    private RoundDAO roundDAO;
    @Autowired
    private RoundScoreDAO roundScoreDAO;
    @Autowired
    private SeminarDAO seminarDAO;
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private KlassSeminarDAO klassSeminarDAO;
    @Autowired
    private KlassDAO klassDAO;

    public Map<String,SeminarListVO> listRoundNameAndSeminar(long courseId, long klassId) {
        //获得该课程下所有讨论课轮次
        List<Round> rounds=roundDAO.listByCourseId(courseId);
        SeminarListVO seminarListVO=new SeminarListVO();
        Map<String,SeminarListVO> map=new HashMap<String,SeminarListVO>();
        for (int i=0;i<rounds.size();i++) {
            //获得每个roundId的班级,对应关系
            List<KlassRound> klassRound = roundDAO.listKlassRoundsByRoundId(rounds.get(i).getId());
            for (int j = 0; j < klassRound.size(); j++) {
                //如果roundid对应的班级是学生的班级
                if (klassRound.get(i).getKlassId() == klassId) {
                    //把讨论课序号存到roundName中
                    String roundName = roundDAO.getByRoundId(klassRound.get(i).getRoundId()).getRoundSerial().toString();
                    // 通过roundId找班级下所有seminar
                    List<Seminar> seminars = seminarDAO.listByRoundId(rounds.get(i).getId());
                    List<SeminarVO> seminarVOS = new LinkedList<SeminarVO>();
                    for (int k = 0; k < seminars.size(); k++) {
                        //把seminarslist变成seminarlistVO
                        Seminar seminar = seminars.get(i);
                        SeminarVO seminarVO = new SeminarVO();
                        seminarVO.setSeminarTopic(seminar.getSeminarName());
                        seminarVO.setSeminarOrder(seminar.getSeminarSerial());
                        seminarVOS.add(seminarVO);
                    }
                    //最后把seminarVOlist放到SeminarListVO中
                    seminarListVO.setSeminarVOList(seminarVOS);
                    //加入map
                    map.put(roundName, seminarListVO);
                }
            }
        }
        return map;
    }

    public List<RoundVO> listRoundByCourseId(long courseId){
        //获得该课程下所有讨论课轮次
        List<Round> rounds=roundDAO.listByCourseId(courseId);
        List<RoundVO> roundVOList=new ArrayList<>();
        for(int i=0;i<rounds.size();i++){//每一轮下
            List<Seminar> seminars=seminarDAO.listByRoundId(rounds.get(i).getId());
            List<SeminarVO> seminarVOList=new ArrayList<>();
            for(int j=0;j<seminars.size();j++){//每一讨论课下
                SeminarVO seminarVO=new SeminarVO();
                seminarVO.setSeminarId(seminars.get(j).getId());
                seminarVO.setSeminarTopic(seminars.get(j).getSeminarName());
                //所有班级id
                List<KlassSeminar> klassSeminars=klassSeminarDAO.listBySeminarId(seminars.get(j).getId());
                List<KlassVO> klassVOList=new ArrayList<>();
                for(int k=0;k<klassSeminars.size();k++){
                    //通过id找klass
                    Klass klass=klassDAO.getByKlassId(klassSeminars.get(k).getKlassId());
                    KlassVO klassVO=new KlassVO();
                    klassVO.setKlassId(klass.getId());
                    klassVO.setGrade(klass.getGrade());
                    klassVO.setKlassSerial(klass.getKlassSerial());
                    klassVO.setKlassName(klass.getGrade(),klass.getKlassSerial());
                    klassVOList.add(klassVO);
                }
                seminarVO.setKlassVOList(klassVOList);
                seminarVOList.add(seminarVO);
            }
            RoundVO roundVO=new RoundVO();
            roundVO.setRoundId(rounds.get(i).getId());
            roundVO.setRoundNumber(rounds.get(i).getRoundSerial());
            roundVO.setSeminarVOList(seminarVOList);
            roundVOList.add(roundVO);
        }
        return roundVOList;
    }

    public Map<RoundVO,List<KlassVO>> getRoundById(long round_id){
        List<Seminar> seminars=seminarDAO.listByRoundId(round_id);
        Round round=roundDAO.getByRoundId(round_id);
        List<SeminarVO> seminarVOList=new ArrayList<>();
        RoundVO roundVO=new RoundVO();
        roundVO.setRoundId(round_id);
        roundVO.setRoundNumber(round.getRoundSerial());
        roundVO.setPresentationScoreMethod(round.getPresentationScoreMethod());
        roundVO.setQuestionScoreMethod(round.getQuestionScoreMethod());
        roundVO.setReportScoreMethod(round.getReportScoreMethod());
//        得到该round下的所有klass
        List<KlassRound> klassRounds=roundDAO.listKlassRoundsByRoundId(round_id);
        List<KlassVO> klassVOList=new ArrayList<>();
        for(int j=0;j<klassRounds.size();j++){
            Klass klass=klassDAO.getByKlassId(klassRounds.get(j).getKlassId());
            KlassVO klassVO=new KlassVO();
            klassVO.setKlassName(klass.getGrade(),klass.getKlassSerial());
            klassVOList.add(klassVO);
        }
        for(int i=0;i<seminars.size();i++){
            SeminarVO s=new SeminarVO();
            s.setSeminarTopic(seminars.get(i).getSeminarName());
            s.setSeminarId(seminars.get(i).getId());
            seminarVOList.add(s);
        }
        roundVO.setSeminarVOList(seminarVOList);
        Map<RoundVO,List<KlassVO>> map=new HashMap<>();
        map.put(roundVO,klassVOList);
        return map;
    }

    public void updateRound(RoundVO round){
        long round_id=round.getRoundId();
        List<KlassRound> klassRounds=roundDAO.listKlassRoundsByRoundId(round_id);
        for (int i=0;i<klassRounds.size();i++)
            klassDAO.updateEnrollNumberByRoundIdAndKlassId(klassRounds.get(i).getEnrollNumber(),klassRounds.get(i).getKlassId(),klassRounds.get(i).getRoundId());
    }
}
