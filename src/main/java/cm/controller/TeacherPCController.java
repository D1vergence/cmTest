package cm.controller;

import cm.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cm/pc/teacher")
public class TeacherPCController {
    /*
    TeacherService teacherService=new TeacherService();
    CourseService courseService=new CourseService();
    KlassService klassService=new KlassService();
    RoundService roundService=new RoundService();
    SeminarService seminarService=new SeminarService();

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String teacherPCIndex(Model model){
        long teacherId;
        model.addAttribute("courseList",courseService.findCoursesByTeacherId());
        return "pc_teacher_index";
    }

    @RequestMapping(value = "/course",method = RequestMethod.POST)
    public String teacherPCCourse(long courseId,Model model){
        model.addAttribute("Course",courseService.findCourseById(courseId));
        return "pc_teacher_course";
    }

    @RequestMapping(value = "/course/importStudent",method = RequestMethod.POST)
    public String teacherPCImportStudents(long courseId,Model model){
        model.addAttribute("klassList",klassService.findKlassesByCourseId(courseId));
        return "pc_teacher_import";
    }

    @RequestMapping(value = "/course/importStudent/submit",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherPCImportStudentSubmit(long klassId, MultipartFile file){
        if(klassService.importStudent(klassId,file))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //重置按钮
    @RequestMapping(value = "/course/importStudent/reset",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherPCImportStudentReset(long klassId){
        klassService.resetStudent(klassId);
        return new ResponseEntity(HttpStatus.OK);
    }


/////////讨论课
    @RequestMapping(value = "/course/seminar",method = RequestMethod.POST)
    public String teacherPCSeminar(long courseId,Model model){
        model.addAttribute("roundAndSeminarList",roundService.findRoundsByCourseId(courseId));
        return "pc_teacher_seminar";//////////////////////
    }

    ////讨论课进入按钮
    @RequestMapping(value = "/course/seminar/info",method = RequestMethod.POST)
    public String teacherPCSeminarInfo(long seminarId,Model model){
        model.addAttribute("AttendanceList",seminarService.fingAttendance(seminarId));
        return "pc_teacher_seminarInfo";
    }

    @RequestMapping(value = "/course/seminar/info/download",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherPCPPTDownload(String filename){
        if(seminarService.downloadPPT(filename))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

////////////导出成绩按钮
    @RequestMapping(value = "/course/exportScore",method = RequestMethod.POST)
    public String teacherPCExportScore(long courseId,Model model){
        List<Map<String,Object>> maps=seminarService.findScoreMap(courseId);
        model.addAttribute("scoreList",maps);
        return "pc_teacher_score";
    }
    */
}
