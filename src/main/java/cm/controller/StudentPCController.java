package cm.controller;

import cm.entity.Course;
import cm.service.CourseService;
import cm.service.RoundService;
import cm.service.SeminarService;
import cm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/cm/pc/student")
public class StudentPCController {
/*
    @Autowired
    StudentService studentService;
    @Autowired
    RoundService roundService;
    @Autowired
    SeminarService seminarService;
    @Autowired
    CourseService courseService;

    ///////////index
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String studentPcIndex(Model model){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        studentService.setStudent(user);
        model.addAttribute("courseList",courseService.listCourseAndKlassByStudentId(studentService.getStudent().getId()));
        return "pc_student_index";
    }

    //////
    @RequestMapping(value = "/course",method = RequestMethod.POST)
    public String studentPCCourse(long courseId,Model model){
        model.addAttribute("curCourse",courseService.findCourseById(courseId));
        return "pc_student_course";
    }

    @RequestMapping(value = "/course/seminar",method = RequestMethod.POST)
    public String studentPCSeminar(long courseId,Model model){
        model.addAttribute("roundAndSeminarList",roundService.findRoundsByCourseId(courseId));
        return "pc_student_seminar";
    }

    @RequestMapping(value = "/course/exportScore",method = RequestMethod.POST)
    public String studentPCScore(long courseId,Model model){
        List<Map<String,Object>> maps=seminarService.findScoreMap(courseId);
        model.addAttribute("scoreList",maps);
        return "pc_student_score";
    }

    @RequestMapping(value = "/course/seminar/info",method=RequestMethod.POST)
    public String studentPCSeminarInfo(long seminarId,Model model){
        model.addAttribute("AttendanceList",seminarService.findAttendance(seminarId));
        return "pc_student_seminarInfo";
    }

    @RequestMapping(value = "/course/seminar/info/register",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity studentPCSeminarRegister(long teamId,long seminarId,int order){
        seminarService.enroll(teamId,seminarId,order);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/course/seminar/info/cancel",method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity studentPCSeminarUnenroll(long teamId,long seminarId){
        if(seminarService.unenroll(teamId,seminarId))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/course/seminar/info/upload",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity studnetPCSeminarPPTUpload(long seminarId, long teamId, MultipartFile file){
        if(seminarService.uploadPPT(seminarId,teamId,file))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/course/seminar/info/download",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity studentPCSeminarPPTDownload(long teamId,long seminarId,String fileName){
        if(seminarService.download(seminarId,teamId,fileName))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
*/
}
