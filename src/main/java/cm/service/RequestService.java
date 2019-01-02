package cm.service;

import cm.dao.*;
import cm.entity.*;
import cm.vo.RequestListVO;
import cm.vo.RequestTypeVO;
import cm.vo.SimpleRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/23
 */
@Service
public class RequestService {
    @Autowired
    private TeamValidApplicationDAO teamValidApplicationDAO;

    @Autowired
    private ShareSeminarDAO shareSeminarDAO;

    @Autowired
    private ShareTeamDAO shareTeamDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private KlassDAO klassDAO;

    @Autowired
    private TeamService teamService;

    public SimpleRequestVO setRequest(SimpleShareApplication simpleShareApplication) {
        SimpleRequestVO simpleRequestVO = new SimpleRequestVO();
        simpleRequestVO.setRequestId(simpleShareApplication.getId());
        simpleRequestVO.setCourseName((courseDAO.getByCourseId(simpleShareApplication.getMainCourseId())).getCourseName());
        simpleRequestVO.setSubCourseName(courseDAO.getByCourseId(simpleShareApplication.getSubCourseId()).getCourseName());
        simpleRequestVO.setSubCourseTeacherName(teacherDAO.getByCourseId(simpleShareApplication.getSubCourseId()).getTeacherName());
        simpleRequestVO.setStatus(simpleShareApplication.getStatus());
        return simpleRequestVO;
    }



    /**
     * 查看所有request
     * @param teacherId
     * @return cm.vo.RequestListVO
     */
    public RequestListVO findRequestListByTeacherId(Long teacherId){
        RequestListVO result=new RequestListVO();
        RequestTypeVO requestTypeVO=new RequestTypeVO();
        RequestTypeVO historyRequestTypeVO=new RequestTypeVO();
        List<SimpleRequestVO> teamRequestVOList1=new ArrayList<>();
        List<SimpleRequestVO> teamRequestVOList2=new ArrayList<>();
        List<SimpleRequestVO> seminarRequestVOList1=new ArrayList<>();
        List<SimpleRequestVO> seminarRequestVOList2=new ArrayList<>();
        List<SimpleRequestVO> validRequestVOList1=new ArrayList<>();
        List<SimpleRequestVO> validRequestVOList2=new ArrayList<>();

        for (ShareTeamApplication shareTeamApplication:shareTeamDAO.listBySubCourseTeacherId(teacherId)) {

            SimpleRequestVO simpleRequestVO = setRequest(shareTeamApplication);

            if(null==simpleRequestVO.getStatus()){
                teamRequestVOList1.add(simpleRequestVO);
            }
            else {
                teamRequestVOList2.add(simpleRequestVO);
            }
        }
        for (ShareSeminarApplication shareSeminarApplication:shareSeminarDAO.listBySubCourseTeacherId(teacherId)) {

            SimpleRequestVO simpleRequestVO = setRequest(shareSeminarApplication);

            if(null==simpleRequestVO.getStatus()){
                seminarRequestVOList1.add(simpleRequestVO);
            }
            else {
                seminarRequestVOList2.add(simpleRequestVO);
            }
        }

        for (TeamValidApplication teamValidApplication:teamValidApplicationDAO.listByTeacherId(teacherId)) {
            SimpleRequestVO simpleRequestVO=new SimpleRequestVO();
            simpleRequestVO.setRequestId(teamValidApplication.getId());
            Team team=teamDAO.getByTeamId(teamValidApplication.getTeamId());

            simpleRequestVO.setKlassName(klassDAO.getKlassNameByKlassId(team.getKlassId()));
            simpleRequestVO.setLeaderName(studentDAO.getByStudentId(team.getLeaderId()).getStudentName());
            simpleRequestVO.setReason(teamValidApplication.getReason());
            simpleRequestVO.setStatus(teamValidApplication.getStatus());
            if(null==simpleRequestVO.getStatus()){
                validRequestVOList1.add(simpleRequestVO);
            }
            else {
                validRequestVOList2.add(simpleRequestVO);
            }
        }

        requestTypeVO.setTeamRequestVOList(teamRequestVOList1);
        requestTypeVO.setSeminarRequestVOList(seminarRequestVOList1);
        requestTypeVO.setValidRequestVOList(validRequestVOList1);
        historyRequestTypeVO.setTeamRequestVOList(teamRequestVOList2);
        historyRequestTypeVO.setSeminarRequestVOList(seminarRequestVOList2);
        historyRequestTypeVO.setValidRequestVOList(validRequestVOList2);
        result.setRequestTypeVO(requestTypeVO);
        result.setHistoryRequestTypeVO(historyRequestTypeVO);
        return result;
    }

    @Transactional
    public Boolean changeTeamShareStatus(Long teacherId,Long teamShareId,boolean handle){
        ShareTeamApplication shareTeamApplication=shareTeamDAO.getByShareTeamIdAndTeacherId(teamShareId,teacherId);
        if (shareTeamApplication == null) {
            return null;
        }
        try {
            Byte status=0;
            if (handle==false){
                status=0;
            }
            else{
                /**
                 * 同意申请
                 */
                status=1;
                /**
                 * 旧的course中的team
                 */
                List<Team>teamList=teamDAO.listByCourseId(shareTeamApplication.getSubCourseId());
                for (Team team:teamList) {
                    teamService.deleteTeam(team.getId());
                }
                /**
                 * 主课程的teamList
                 */
                List<Team>teamList1=teamDAO.listByCourseId(shareTeamApplication.getMainCourseId());
                for (Team team:teamList1) {
                    //moveTeam(team,shareTeamApplication.getSubCourseId());
                }
            }
            shareTeamDAO.updateStatusByShareTeamId(status,teamShareId);

        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * 在subcourse建新team
     * @param team
     * @param subCourseId
     * @return void
     */
//    private void moveTeam(Team team,Long subCourseId){
//        List<Long>longList=new ArrayList<>();
//        for (Student student:team.getStudents()) {
//            Long klassId=klassDAO.getKlassIdByCourseIdAndStudentId(subCourseId,student.getId());
//            longList.add(klassId);
//        }
//        team.setKlassId(CountUtils.maxCount(longList));
//        team.setCourseId(subCourseId);
//        teamService.createTeam(team);
//    }

}
