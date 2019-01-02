package cm.controller;

import cm.service.*;
import cm.vo.CourseDetailVO;
import cm.vo.KlassVO;
import cm.vo.SeminarScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cm/teacher/course")
public class TeacherCourseController {
    @Autowired
    public static CourseService courseService;
    @Autowired
    public static TeamService teamService;
    @Autowired
    public static KlassService klassService;

    //课程管理
    @RequestMapping(method= RequestMethod.GET)
    public String teacherCourseManage(Model model) {
        model.addAttribute("courseList", courseService.listCourseByTeacherId());
        return "teacher_courseList";
    }

    /////////////////////////////////////课程详情页
    @RequestMapping(value="/info",method = RequestMethod.POST)
    public String teacherCourseInfo(Long courseId,Model model){
        courseService.setCourse(courseId);
        model.addAttribute("curCourse",courseService.getCourse());
        //model.addAttribute("TeamNeedVO",courseService.getTeamNeedVO(courseId));
        return "teacher_courseInfo";
    }

    ////////////////////////////////创建课程
    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public String teacherCourseCreate(){
        return "teacher_course_create";
    }
////////////////////////////////创建课程
    @RequestMapping(value = "/create",method = RequestMethod.PUT,consumes ="application/json" )
    @ResponseBody
    public ResponseEntity teacherCourseCreateSubmit(@RequestBody CourseDetailVO course){
        if(courseService.addCourse(course))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }

    //////////////////////////////删除课程
    @RequestMapping(value = "/{courseId}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity teacherCourseDelete(@PathVariable long courseId){
        courseService.deleteCourseById(courseId);
            return new ResponseEntity(HttpStatus.OK);
    }

//    //////////////////修改课程
//    @RequestMapping(value = "",method = RequestMethod.PATCH)
//    @ResponseBody
//    public ResponseEntity teacherCourseUpdate(@RequestBody CourseVO course){
//        courseService.updateCourse(course);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    //////////////学生组队
    @RequestMapping(value="/teamList",method = RequestMethod.POST)
    public String teacherTeamList(Long courseId,Model model){
        courseService.setCourse(courseId);
        model.addAttribute("teamList",teamService.listTeamByCourseId(courseId));
        return "teacher_teamList";
    }

    //////学生成绩
    @RequestMapping(value = "/grade",method = RequestMethod.POST)
    public String teacherGrade(Long courseId,Model model){
        courseService.setCourse(courseId);
        Map<String, List<SeminarScoreVO>> maps=courseService.listScoreForTeacher(courseId);
        model.addAttribute("roundNameAndSeminarScore",maps);
        return "teacher_grade.html";
    }

    ////////班级管理
    @RequestMapping(value="/klassList",method = RequestMethod.POST)
    public String teacherKlassManage(Long course_id,Model model){
        courseService.setCourse(course_id);
        model.addAttribute("klassList",klassService.listKlassByCourseId(course_id));
        return "teacher_klassList";
    }

    //////////创建班级
    @RequestMapping(value = "/klass/create",method = RequestMethod.GET)
    public String teacherKlassCreate(){
        return "teacher_klass_create";
    }

    @RequestMapping(value = "/klass/create",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherKlassCreateSubmit(@RequestBody KlassVO klassVO){
        if(klassService.addKlass(klassVO))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }

    ////////删除班级
    @RequestMapping(value = "/klass/{klassId}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity teacherKlassDelete(@PathVariable long klassId) {
        klassService.deleteKlassById(klassId);
        return new ResponseEntity(HttpStatus.OK);
    }

    ///////提交学生名单
    @RequestMapping(value = "/klass",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherKlassStudentListUpload(Long klassId, MultipartFile fileUpload){
        if(klassService.uploadStudentList(klassId,fileUpload)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else
            return new ResponseEntity(HttpStatus.CONFLICT);
    }
}
