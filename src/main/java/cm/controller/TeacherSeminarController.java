package cm.controller;

import cm.service.CourseService;
import cm.service.KlassService;
import cm.service.RoundService;
import cm.service.SeminarService;
import cm.vo.RoundVO;
import cm.vo.SeminarInfoVO;
import cm.vo.SeminarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cm/teacher/course/seminar")
public class TeacherSeminarController {
    @Autowired
    public static SeminarService seminarService;
    @Autowired
    public static RoundService roundService;

    KlassService klassService=TeacherCourseController.klassService;
    CourseService courseService= TeacherCourseController.courseService;

    ////////讨论课管理
    ///////////讨论课列表
    @RequestMapping(value = "",method = RequestMethod.POST)
    public String teacherSeminar(Long courseId,Model model){
        courseService.setCourse(courseId);
        model.addAttribute("roundList",roundService.listRoundByCourseId(courseId));
        model.addAttribute("klassList",klassService.listKlassByCourseId(courseId));
        model.addAttribute("courseName",courseService.getCourse().getCourseName());
        return "teacher_seminarList";
    }

    ///////////讨论课
    @RequestMapping(value ="/course",method = RequestMethod.GET)
    public String teacherSeminarCourseList(Model model){
        model.addAttribute("courseList",courseService.listCourseByTeacherId());
        model.addAttribute("seminarInfo",seminarService.getSeminarInfoING());
        return "teacher_seminar_courseList";
    }


    ////////////////////获取轮次设置
    @RequestMapping(value = "/setting",method = RequestMethod.POST)
    public String teacherSeminarSetting(long round_id,Model model){
        model.addAttribute("Round",roundService.getRoundById(round_id));
        return "roundSetting";
    }

    //////////////////修改轮次设置
    @RequestMapping(value = "/setting",method = RequestMethod.PATCH,consumes = "application/json")
    @ResponseBody
    public ResponseEntity teacherSeminarSettingSubmit(@RequestBody RoundVO round) {
        roundService.updateRound(round);
        return new ResponseEntity(HttpStatus.OK);
    }

    ////////////////////创建讨论课
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherSeminarCreate(@RequestBody SeminarInfoVO seminar){
        seminarService.addSeminar(seminar);
        return new ResponseEntity(HttpStatus.OK);
    }

    //////////讨论课详情
    @RequestMapping(value = "/info",method = RequestMethod.POST)
    public String teacherSeminar(long klassId,long seminarId,Model model){
        seminarService.setKlasSeminar(seminarService.getKlassSeminarVO(klassId,seminarId));
        model.addAttribute("Seminar",seminarService.getSeminarInfo(seminarId));
        return "teacher_seminarInfo";
    }

    /////////讨论课修改
    @RequestMapping(value="",method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity teacherSeminarUpdate(@RequestBody SeminarInfoVO seminar){
        seminarService.updateSeminar(seminar);
        return new ResponseEntity(HttpStatus.OK);
    }

    ///////////////删除讨论课
    @RequestMapping(value = "/{seminarId}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity teacherSeminarDelete(@PathVariable long seminarId){
        seminarService.deleteSeminarById(seminarId);
        return new ResponseEntity(HttpStatus.OK);
    }

    ///////////////讨论课报名列表
    @RequestMapping(value = "/enrollList",method = RequestMethod.POST)
    public String teacherSeminarEnrollList(Model model){
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo());
        return "teacher_seminar_enrollList";
    }

    ///////////////////讨论课正在进行websocket
    @RequestMapping(value = "/progressing",method = RequestMethod.GET)
    public String teacherSeminarProgressing(Model model){
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo());
        return "teacher_seminar_progressing";
    }

    //////讨论课结束报告页
    @RequestMapping(value = "/finished",method = RequestMethod.GET)
    public String teacherSeminarFinished(Model model){
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo());
        return "teacher_seminar_finished";
    }

    //////讨论课报告打分页
    @RequestMapping(value = "/finished",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherSeminarReportScoreSubmit(List<BigDecimal> score){
        seminarService.scoreReport(score);
        return new ResponseEntity(HttpStatus.OK);
    }


    /////////////////讨论课分数
    @RequestMapping(value ="/grade",method = RequestMethod.POST)
    public String teacherSeminarGrade(long klassSeminarId,Model model){
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo(klassSeminarId));
        return "teacher_seminar_grade";
    }
}
