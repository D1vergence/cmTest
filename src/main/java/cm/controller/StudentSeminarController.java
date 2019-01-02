package cm.controller;

import cm.service.*;
import cm.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cm/student/seminar")
public class StudentSeminarController {
    @Autowired
    private SeminarService seminarService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeamService teamService;

    UserVO student;

    public static CourseDetailVO courseDetailVO;

        //////student course List
    @RequestMapping(value = "/seminarEntrance",method = RequestMethod.GET)
    public String studentSeminarEntrance(Model model){
        student= UserController.userVO;
        Map<CourseVO, KlassVO>maps=seminarService.listCourseAndKlass(student);
        model.addAttribute("courseAndKlassList",maps);
        return "student_seminar_entrance";
    }

    /////////student seminar List
    @RequestMapping(value = "/List",method=RequestMethod.GET)
    public String studentSeminarList(Long courseId,Long klassId,Model model){
        courseDetailVO=courseService.getCourseById(courseId);
        //String--RoundName
        Map<String, SeminarListVO>maps=roundService.listRoundNameAndSeminar(courseId,klassId);
        model.addAttribute("roundAndSeminarList",maps);
        return "student_seminarList";
    }

    ///////student seminar info
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public String studentSeminarInfo(Long klassId,Long seminarId,Model model){
        model.addAttribute("courseName",courseDetailVO.getCourseName());
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo(klassId,seminarId));
        model.addAttribute("attendance",seminarService.getAttendance(klassId,seminarId,student.getId()));
        return "student_seminar_info";
    }

    //////student enroll List
    @RequestMapping(value = "/enrollList",method = RequestMethod.GET)
    public String studentSeminarEnrollList(Model model,Long klassSeminarId){
        SeminarInfoVO seminarInfoVO=seminarService.getSeminarInfo(klassSeminarId);
        model.addAttribute("seminarInfo",seminarInfoVO);
        model.addAttribute("team",teamService.getMyTeam(courseService.getCourse().getId(),student.getId()));

        return "student_seminar_enrollList";
    }


    //////student enroll
    @RequestMapping(value = "/enrollList/enroll",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity studentSeminarEnroll(Long klassSeminarId, AttendanceVO attendance){
        if(seminarService.enroll(klassSeminarId,attendance,student.getId()))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }

    //////student enroll cancel
    @RequestMapping(value = "/enroll",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity studentSeminarUnEnroll(Long klassSeminarId){
        if(seminarService.unenroll(klassSeminarId,student.getId()))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }

//    //////student ppt getPPT
//    @RequestMapping(value = "/PPT",method=RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity studentSeminarPPT(Long klassSeminarId,String PPTurl){
//        if(seminarService.getPPT(klassSeminarId,PPTurl))
//            return new ResponseEntity(HttpStatus.OK);
//        else
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }

    /////student ppt upload
    @RequestMapping(value = "/PPT",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity studentSeminarPPTUpload(Long klassSeminarId, MultipartFile file,AttendanceVO attendance){
        if(seminarService.uploadPPT(klassSeminarId,file,attendance))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }

    /**
     * 学生-讨论课-查看成绩
     * @param klassSeminarId
     * @param klassId
     * @param model
     * @return
     */
    //////student seminar score
    @RequestMapping(value = "/score",method = RequestMethod.GET)
    public String studentSeminarScore(Long klassSeminarId,Long klassId,Model model){
        model.addAttribute("seminarScore",seminarService.getSeminarScore(klassId,klassSeminarId,student.getId()));
        return "student_seminar_grade";
    }

    //////student enter seminar
    @RequestMapping(value = "/progressing",method = RequestMethod.GET)
    public String studentSeminarProgress(Long klassSeminarId,Model model){
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo(klassSeminarId));
        model.addAttribute("teamId",seminarService.getPresentatingTeamId(klassSeminarId));
        return "student_seminar_progress";
    }
}
