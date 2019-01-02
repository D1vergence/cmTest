package cm.controller;

import cm.dao.StudentDAO;
import cm.dao.TeamDAO;
import cm.dao.QuestionDAO;
import cm.entity.Attendance;
import cm.entity.Question;
import cm.entity.Team;
import cm.entity.Student;
import cm.service.AttendanceService;
import cm.service.TeamService;
import cm.service.StudentService;
import cm.vo.NextAttendanceVO;
import cm.vo.SelectedQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.*;
import java.lang.Long;
/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/31
 */
@Component
public class WebSocketController {

    @Autowired
    TeamDAO teamDao;

    @Autowired
    StudentDAO studentDao;

    @Autowired
    QuestionDAO QuestionDao;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 记录临时问题
     */
    List<Question> tempQuestionList;
    @MessageMapping("/student/add/{klassSeminarId}/{attendanceId}/{teamId}/{studentId}")
    @SendTo("/topic/broadcast")
    public int addTempQuestion(@DestinationVariable("klassSeminarId") String klassSeminarId,
                                @DestinationVariable("attendanceId") String attendanceId,
                                @DestinationVariable("teamId") String teamId,
                                @DestinationVariable("studentId") String studentId) {
        Question tempQuestion=null;
        System.out.println("正在添加一个临时问题");
        tempQuestion.setKlassSeminarId(Long.parseLong(klassSeminarId));

        tempQuestion.setAttendanceId(Long.parseLong(attendanceId));

        tempQuestion.setTeamId(Long.parseLong(teamId));

        tempQuestion.setStudentId(Long.parseLong(studentId));
        tempQuestionList.add(tempQuestion);
        return 1;
    }

    /**
     * 挑选临时问题
     * @return selectedQuestionVO
     */
    @MessageMapping("/teacher/select/{klassSeminarId}")
    @SendTo("/topic/broadcast")
    public SelectedQuestionVO selectTempQuestion(@DestinationVariable("klassSeminarId") String klassSeminarId,
                                                 @DestinationVariable("attendanceId") String attendanceId) {
        System.out.println("正在挑选一个临时问题");
        List<Question> currentQuestionList = null;
        for(int i=0;i<tempQuestionList.size();i++)
        {
            if(tempQuestionList.get(i).getKlassSeminarId() == Long.parseLong(klassSeminarId) &&
                    tempQuestionList.get(i).getAttendanceId() == Long.parseLong(attendanceId))
                currentQuestionList.add(tempQuestionList.get(i));
        }
        if(currentQuestionList != null){
            int random = (int)(Math.random()*currentQuestionList.size());
            Question selectedQuestion=null;
            selectedQuestion = currentQuestionList.get(random);
            currentQuestionList.remove(random);
            Byte by = new Byte("1");
            selectedQuestion.setIsSelected(by);
            QuestionDao.createQuestion(selectedQuestion.getKlassSeminarId(), selectedQuestion.getAttendanceId(), selectedQuestion);

            SelectedQuestionVO selectedQuestionVO = null;
            StudentService studentService = null;
            Student student = studentService.getByStudentId(selectedQuestion.getStudentId());
            selectedQuestionVO.setStudentName(student.getStudentName());
            selectedQuestionVO.setStudentAccount(student.getAccount());
            TeamService teamService = null;
            Team team = teamService.getByTeamId(selectedQuestion.getTeamId());
            selectedQuestionVO.setTeamNumber(Integer.valueOf(team.getKlassSerial()), Integer.valueOf(team.getTeamSerial()));

            for(int i=0;i<tempQuestionList.size();i++)
            {
                if(tempQuestionList.get(i).getId() == selectedQuestion.getId())
                    tempQuestionList.remove(i);
            }
            selectedQuestionVO.setStatus(1);
            return selectedQuestionVO;
        }
        else
        {
            SelectedQuestionVO selectedQuestionVO = null;
            selectedQuestionVO.setStatus(0);
            return selectedQuestionVO;
        }
    }

    @MessageMapping("/nextAttendance/{teamOrder}/{klassSeminarId}/{teamId}")
    @SendTo("/topic/broadcast")
    public NextAttendanceVO nextAttendance(@DestinationVariable("klassSeminarId") String klassSeminarId,
                                           @DestinationVariable("teamId") String teamId,
                                           @DestinationVariable("teamOrder") String teamOrder){

        Attendance currentAttendance = attendanceService.getByKlassSeminarIdAndTeamId(Long.parseLong(klassSeminarId),Long.parseLong(teamId));
        Attendance nextAttendance = attendanceService.getByKlassSeminarIdAndTeamOrder(currentAttendance.getKlassSeminarId(),currentAttendance.getTeamOrder()+1);
        if(nextAttendance != null) {
            NextAttendanceVO nextAttendanceVO = null;
            nextAttendanceVO.setKlassSeminarId(String.valueOf(nextAttendance.getKlassSeminarId()));
            nextAttendanceVO.setTeamOrder(String.valueOf(nextAttendance.getTeamOrder()));
            nextAttendanceVO.setTeamId(String.valueOf(nextAttendance.getTeamId()));
            TeamService teamService = null;
            Team team = teamService.getByTeamId(nextAttendance.getTeamId());
            nextAttendanceVO.setTeamNumber(Integer.valueOf(team.getKlassSerial()), Integer.valueOf(team.getTeamSerial()));
            nextAttendanceVO.setStatus(1);
            return nextAttendanceVO;
        }
        else{
            NextAttendanceVO nextAttendanceVO = null;
            nextAttendanceVO.setStatus(0);
            return nextAttendanceVO;
        }
    }
}
