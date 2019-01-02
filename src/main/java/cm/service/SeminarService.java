package cm.service;

import cm.dao.*;
import cm.entity.*;
import cm.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SeminarService {
    @Autowired
    private SeminarDAO seminarDAO;
    @Autowired
    private KlassSeminarDAO klassSeminarDAO;
    @Autowired
    private AttendanceDAO attendanceDAO;
    @Autowired
    private SeminarScoreDAO seminarScoreDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private KlassDAO klassDAO;
    @Autowired
    private RoundDAO roundDAO;
    @Autowired
    private TeamDAO teamDAO;

    static Course course;
    static Klass klass;
    static KlassSeminar klassSeminar;

    public boolean addSeminar(Seminar seminar){

        seminarDAO.createSeminar(seminar);
        return true;
    }

    public KlassSeminar findKlassSeminarById(Long klassId, Long seminarId){
        return klassSeminarDAO.getBySeminarIdAndKlassId(seminarId,klassId);
    }


    public Seminar TransferSeminarInfoToSeminar(SeminarInfoVO seminarInfoVO){
        Seminar seminar=new Seminar();
        Timestamp startTime=Timestamp.valueOf(seminarInfoVO.getEnrollStartTime());
        Timestamp endTime=Timestamp.valueOf(seminarInfoVO.getEnrollEndTime());
        seminar.setCourseId(course.getId());
        seminar.setSeminarName(seminarInfoVO.getSeminarName());
        seminar.setEnrollEndTime(endTime);
        seminar.setEnrollStartTime(startTime);
        seminar.setIntroduction(seminarInfoVO.getIntroduction());
        seminar.setIsVisible(seminarInfoVO.getIsVisible());
        seminar.setMaxTeam(seminarInfoVO.getTeamNumLimit());

        return seminar;
    }
    public void addSeminar(SeminarInfoVO seminarInfoVO){
        Seminar seminar=TransferSeminarInfoToSeminar(seminarInfoVO);
        seminarDAO.createSeminar(seminar);
    }

    public void updateSeminar(SeminarInfoVO seminarInfoVO){
        Seminar seminar=TransferSeminarInfoToSeminar(seminarInfoVO);
        seminarDAO.modifySeminar(seminar);
    }

    //删除讨论课除了Seminar表应该还有其他？？？
    public void deleteSeminarById(Long seminarId){
        List<KlassSeminar> klassSeminars=klassSeminarDAO.listBySeminarId(seminarId);
        for (int i=0;i<klassSeminars.size();i++)
            klassSeminarDAO.deleteByKlassSeminarId(klassSeminars.get(i).getId());
        seminarDAO.deleteBySeminarId(seminarId);
    }

    public List<Attendance> findAttendanceById(Long klassSeminarId){
        return attendanceDAO.listByKlassSeminarId(klassSeminarId);
    }

//    public List<SeminarScore> findSeminarScoreById(Long klassSeminarId){
//        return seminarScoreDAO.listByKlassSeminarId(klassSeminarId);
//    }

    public boolean enroll(Long klassSeminarId,AttendanceVO attendance,Long studentId){
        KlassSeminar klassSeminar=klassSeminarDAO.getByKlassSeminarId(klassSeminarId);
        Team team=teamDAO.getByKlassIdAndStudentId(klassSeminar.getKlassId(),studentId);
        Attendance attendance1=new Attendance();
        attendance1.setTeamOrder(Integer.valueOf(attendance.getTeamOrder()));
        attendance1.setIsPresent(attendance.getIsPresent());
        attendance1.setReportUrl(null);
        attendanceDAO.addAttendance(attendance1,klassSeminarId,team.getId());
        return true;
    }

    public boolean unenroll(Long klassSeminarId,Long studentId){
        Attendance attendance=attendanceDAO.getByKlassSeminarIdAndTeamId(klassSeminarId,studentId);
        attendanceDAO.deleteAttendanceByAttendanceId(attendance.getId());
        return true;
    }

    public void setCourse(Long courseId) {
        this.course = courseDAO.getByCourseId(courseId);
    }

    public void setKlass(Long klassId) {
        this.klass = klassDAO.getByKlassId(klassId);
    }

    public Map<CourseVO,KlassVO> listCourseAndKlass(UserVO student) {
        List<Course> courses=courseDAO.listByStudentId(student.getId());
        Map<CourseVO,KlassVO> map=new HashMap<CourseVO,KlassVO>();
        for(int i=0;i<courses.size();i++)
        {
            Klass k=klassDAO.getByCourseIdAndStudentId(courses.get(i).getId(),student.getId());
            KlassVO klassVO=new KlassVO();
            klassVO.setKlassId(k.getId());
            klassVO.setKlassName(k.getGrade(),k.getKlassSerial());
            CourseVO courseVO=new CourseVO();
            courseVO.setId(courses.get(i).getId());
            courseVO.setName(courses.get(i).getCourseName());

            map.put(courseVO,klassVO);
        }
        return map;

    }

    public SeminarInfoVO getKlassSeminarByEachId(Long klassId, Long seminarId) {
        KlassSeminar klassSeminar=klassSeminarDAO.getBySeminarIdAndKlassId(seminarId,klassId);
        SeminarInfoVO seminarInfoVO=getSeminarInfo(klassSeminar.getId());
        return seminarInfoVO;
    }

    public SeminarModifyVO getklassSeminarVO(Long klassId, Long seminarId) {
        KlassSeminar klassSeminar=findKlassSeminarById(klassId,seminarId);
        SeminarModifyVO seminarModifyVO=new SeminarModifyVO();
        Long courseId=klassDAO.getCourseIdByKlassId(klassId);
        Course course=courseDAO.getByCourseId(courseId);
        Seminar seminar=seminarDAO.getBySeminarId(seminarId);
        seminarModifyVO.setCourseName(course.getCourseName());
        seminarModifyVO.setSeminarName(seminar.getSeminarName());
        seminarModifyVO.setIntroduction(seminar.getIntroduction());
        seminarModifyVO.setIsVisible(seminar.getIsVisible());
        seminarModifyVO.setEnrollStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(seminar.getEnrollStartTime()));
        seminarModifyVO.setEnrollEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(seminar.getEnrollEndTime()));
        seminarModifyVO.setTeamNumLimit(seminar.getMaxTeam());
        seminarModifyVO.setRoundSerial(roundDAO.getByRoundId(seminar.getRoundId()).getRoundSerial());

        //获得当前讨论课的所有klass的dll
        List<KlassSeminar> klassSeminars=klassSeminarDAO.listBySeminarId(seminarId);
        List<KlassSeminarReportDDL> reportDDLList=new LinkedList<KlassSeminarReportDDL>();
        //一个个把所有班级的dll放到list中
        for(int i=0;i<klassSeminars.size();i++) {
            KlassSeminarReportDDL klassSeminarReportDDL=new KlassSeminarReportDDL();
            klassSeminarReportDDL.setReportDDL(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                    format(klassSeminars.get(i).getReportDdl()));
            reportDDLList.add(klassSeminarReportDDL);
        }
        seminarModifyVO.setReportDDLList(reportDDLList);
        return seminarModifyVO;
    }
    
    public AttendanceListVO getAttendanceList(Long klassSeminarId) {
        List<AttendanceVO> attendanceVOS=new LinkedList<AttendanceVO>();
        List<Attendance> attendanceList=attendanceDAO.listByKlassSeminarId(klassSeminarId);
        for(int i=0;i<attendanceList.size();i++)
        {
            Attendance attendance=attendanceList.get(i);
            AttendanceVO attendanceVO=new AttendanceVO();
            attendanceVO.setTeamOrder(attendance.getTeamOrder());
            attendanceVOS.add(attendanceVO);
        }
        AttendanceListVO attendanceListVO=new AttendanceListVO();
        attendanceListVO.setAttendanceList(attendanceVOS);
        return attendanceListVO;
    }

    public AttendanceVO getAttendance(Long klassId, Long seminarId, Long studentId) {
        KlassSeminar klassSeminar=klassSeminarDAO.getBySeminarIdAndKlassId(klassId,seminarId);
        AttendanceVO attendanceVO=new AttendanceVO();
        Attendance attendance=attendanceDAO.getByKlassSeminarIdAndKlassIdAndStudentId(klassSeminar.getId(),seminarId,studentId);
        attendanceVO.setAttendanceId(attendance.getId());
        attendanceVO.setTeamOrder(attendance.getTeamOrder());
        attendanceVO.setIsPresent(attendance.getIsPresent());
        Klass klass=klassDAO.getByKlassId(klassId);
        attendanceVO.setKlassName(klass.getGrade(),klass.getKlassSerial());
        return attendanceVO;
    }

    public SeminarInfoVO getSeminarInfo(Long klassId, Long seminarId) {
        KlassSeminar klassSeminar=findKlassSeminarById(klassId,seminarId);
        return getSeminarInfo(klassSeminar.getId());
    }

    public SeminarInfoVO getSeminarInfo(Long klassSeminarId) {
        SeminarInfoVO seminarInfoVO=new SeminarInfoVO();

        seminarInfoVO.setSeminarId(klassSeminarId);
        //通过seminarid找到seminar然后得到intro
        Seminar seminar=seminarDAO.getBySeminarId(klassSeminarId);
        Round round=roundDAO.getByRoundId(seminar.getRoundId());
        seminarInfoVO.setIntroduction(seminar.getIntroduction());
        seminarInfoVO.setRoundSerial(round.getRoundSerial());
        seminarInfoVO.setSeminarName(seminar.getSeminarName());
        seminarInfoVO.setSeminarSerial(seminar.getSeminarSerial());

        seminarInfoVO.setAttendanceListVO(getAttendanceList(klassSeminarId));
        return seminarInfoVO;
    }

    /**
     * 学生讨论课查看成绩
     * @param klassId
     * @param klassSeminarId
     * @param studentId
     * @return
     */
    public SeminarScoreVO getSeminarScore(Long klassId, Long klassSeminarId,Long studentId) {
        SeminarScoreVO seminarScoreVO=new SeminarScoreVO();
        //为了得到seminarid
        KlassSeminar klassSeminar=klassSeminarDAO.getByKlassSeminarId(klassSeminarId);
        //通过klassid找到courseid
        Long courseId=klassDAO.getCourseIdByKlassId(klassId);
        Course course=courseDAO.getByCourseId(courseId);
        //获得班级为了名字
        Klass klass=klassDAO.getByKlassId(klassId);
        //获得attendence
        Attendance attendance=attendanceDAO.getBySeminarIdAndKlassIdAndStudentId(
                klassSeminar.getSeminarId(),klassSeminar.getKlassId(),studentId);
        //获得讨论课
        Seminar seminar=seminarDAO.getBySeminarId(klassSeminar.getSeminarId());
        //获得讨论课分数,为了SimpleSeminarScoreVO
        SeminarScore seminarScore=seminarScoreDAO.getByKlassSeminarIdAndTeamId(
                klassSeminarId,teamDAO.getTeamIdByStudentIdAndKlassId(studentId,klassId));

        seminarScoreVO.setCourseName(course.getCourseName());
        seminarScoreVO.setKlassName(klass.getGrade(),klass.getKlassSerial());
        seminarScoreVO.setTeamOrder(attendance.getTeamOrder());

        SimpleSeminarScoreVO simpleSeminarScoreVO=new SimpleSeminarScoreVO();
        simpleSeminarScoreVO.setSeminarName(seminar.getSeminarName());
        //获得seminar分数
        simpleSeminarScoreVO.setTotalScore(seminarScore.getTotalScore());
        simpleSeminarScoreVO.setReportScore(seminarScore.getReportScore());
        simpleSeminarScoreVO.setQuestionScore(seminarScore.getQuestionScore());
        simpleSeminarScoreVO.setPresentationScore(seminarScore.getPresentationScore());
       //其他有的没的
        simpleSeminarScoreVO.setSeminarName(seminar.getSeminarName());
        simpleSeminarScoreVO.setIntroduction(seminar.getIntroduction());
        simpleSeminarScoreVO.setSeminarId(seminar.getId());
        simpleSeminarScoreVO.setSeminarSerial(seminar.getSeminarSerial());
        //集合1次
        seminarScoreVO.setSimpleSeminarScoreVO(simpleSeminarScoreVO);
        return seminarScoreVO;
    }

    public Long getPresentatingTeamId(Long klassSeminarId) {
        List<Attendance> attendanceList=attendanceDAO.listByKlassSeminarId(klassSeminarId);
        for(int i=0;i<attendanceList.size();i++)
            if(attendanceList.get(i).getIsPresent().equals(1))
            {
                return attendanceDAO.getTeamIdByAttendanceId(attendanceList.get(i).getId());
            }
        return attendanceList.get(0).getId();
    }

    public SeminarInfoVO getSeminarInfoING() {
        List<Round> roundList=roundDAO.listByCourseId(course.getId());
        for(int i=0;i<roundList.size();i++) {
            Round round=roundList.get(i);
            List<Seminar> seminars = seminarDAO.listByRoundId(round.getId());
            for(int j=0;j<seminars.size();j++)
            {
                Seminar seminar=seminars.get(i);
                //通过seminarid获取班级讨论课
                List<KlassSeminar> klassSeminars=klassSeminarDAO.listBySeminarId(seminar.getId());
                for(int k=0;k<klassSeminars.size();k++) {
                    {
                        KlassSeminar klassSeminar = klassSeminars.get(i);
                        if (klassSeminar.getStatus().equals(1)) {
                            SeminarInfoVO seminarInfoVO =getSeminarInfo(klassSeminar.getId());
                            return seminarInfoVO;
                        }
                    }
                }
            }
        }
        //什么讨论课都不在进行
        return null;
    }

    public KlassSeminarVO getKlassSeminarVO(long klassId, long seminarId) {
    KlassSeminar klassSeminar=klassSeminarDAO.getBySeminarIdAndKlassId(seminarId,klassId);
    KlassSeminarVO klassSeminarVO=new KlassSeminarVO();
    Seminar seminar=seminarDAO.getBySeminarId(klassSeminar.getSeminarId());
    Round round=roundDAO.getByRoundId(seminar.getRoundId());
    klassSeminarVO.setTopic(seminar.getSeminarName());
    klassSeminarVO.setRoundSerial(round.getRoundSerial());
    klassSeminarVO.setIntro(seminar.getIntroduction());
    klassSeminarVO.setIsVisible(seminar.getIsVisible());
    klassSeminarVO.setId(klassSeminar.getId());
    klassSeminarVO.setReportDDL(klassSeminar.getReportDdl().toString());
    klassSeminarVO.setTeamNumLimit(seminar.getMaxTeam());
    return klassSeminarVO;
    }

    public void setKlasSeminar(KlassSeminarVO klassSeminarVO) {
        klassSeminar.setId(klassSeminarVO.getId());
    }

    public SeminarInfoVO getSeminarInfo()
    {
        return getSeminarInfo(klassSeminar.getId());
    }

    public void scoreReport(List<BigDecimal> score) {
        List<Attendance> attendanceList=attendanceDAO.listByKlassSeminarId(klassSeminar.getId());
        for(int i=0;i<attendanceList.size();i++) {
            //获取展示的所有小组
            Team team=teamDAO.getByAttendanceId(attendanceList.get(i).getId());
            //得到所有当前分数
            SeminarScore seminarScore=seminarScoreDAO.getByKlassSeminarIdAndTeamId(klassSeminar.getId(),team.getId());
            seminarScoreDAO.updateSeminarScore(seminarScore, klassSeminar.getId(),team.getId());
        }
    }

    public boolean uploadPPT(Long klassSeminarId, MultipartFile file,AttendanceVO attendanceVO) {
        if(file.getContentType().equals("ppt")) {
            attendanceDAO.updatePptByAttendanceId(attendanceVO.getAttendanceId(), file.getName(), file.getResource().toString());
        return true;
        }else
            return false;
    }
}
