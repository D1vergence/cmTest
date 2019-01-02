package cm.controller;

import cm.service.CourseService;
import cm.service.KlassService;
import cm.service.RoundService;
import cm.service.SeminarService;
import cm.vo.*;
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
import java.util.Map;

@Controller
@RequestMapping("/cm/teacher/course/seminar")
public class TeacherSeminarController {
    @Autowired
    private SeminarService seminarService;
    @Autowired
    private RoundService roundService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private KlassService klassService;

    CourseDetailVO courseDetailVO;
    SeminarInfoVO seminarInfoVO;
    ////////讨论课管理
    ///////////讨论课列表
    @RequestMapping(value = "",method = RequestMethod.POST)
    public String teacherSeminar(Long courseId,Model model){
        courseDetailVO=courseService.getCourseById(courseId);
        model.addAttribute("roundList",roundService.listRoundByCourseId(courseId));
        model.addAttribute("klassList",klassService.listKlassByCourseId(courseId));
        model.addAttribute("courseName",courseDetailVO.getCourseName());
        return "teacher_seminarList";
    }

    ///////////讨论课
    @RequestMapping(value ="/course",method = RequestMethod.GET)
    public String teacherSeminarCourseList(Model model){
        model.addAttribute("courseList",courseService.listCourseByTeacherId());
        model.addAttribute("seminarInfo",seminarService.getSeminarInfoING(courseDetailVO));
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
        seminarService.addSeminar(seminar,courseDetailVO);
        return new ResponseEntity(HttpStatus.OK);
    }

    //////////讨论课详情
    @RequestMapping(value = "/info",method = RequestMethod.POST)
    public String teacherSeminar(long klassId,long seminarId,Model model){
        seminarInfoVO=seminarService.getSeminarInfo(klassId,seminarId);
        model.addAttribute("Seminar",seminarInfoVO);
        return "teacher_seminarInfo";
    }

    @RequestMapping(value = "/modify",method = RequestMethod.GET)
    public String teacherSeminarModify(Model model){
        model.addAttribute("seminarModify",seminarService.getSeminarModifyVO(seminarInfoVO.getKlassId(),seminarInfoVO.getSeminarId()));
        return "teacher_seminar_modify";
    }

    /////////讨论课修改
    @RequestMapping(value="",method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity teacherSeminarUpdate(@RequestBody SeminarInfoVO seminar){
        seminarInfoVO=seminar;
        seminarService.updateSeminar(seminar,courseDetailVO);
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
        model.addAttribute("seminarInfo",seminarInfoVO);
        return "teacher_seminar_enrollList";
    }

    ///////////////////讨论课正在进行websocket
    @RequestMapping(value = "/progressing",method = RequestMethod.GET)
    public String teacherSeminarProgressing(Model model){
        model.addAttribute("seminarInfo",seminarInfoVO);
        return "teacher_seminar_progressing";
    }

    //////讨论课结束报告页
    @RequestMapping(value = "/finished",method = RequestMethod.GET)
    public String teacherSeminarFinished(Model model){
        model.addAttribute("seminarInfo",seminarInfoVO);
        return "teacher_seminar_finished";
    }

    //////讨论课报告打分页
    @RequestMapping(value = "/finished",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherSeminarReportScoreSubmit(List<BigDecimal> score){
        seminarService.scoreReport(score,seminarInfoVO);
        return new ResponseEntity(HttpStatus.OK);
    }

    ///////讨论课展示提问打分
    @RequestMapping(value = "/progressing/end",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity teacherSeminarProcessingScore(Map<BigDecimal, Map<Long,BigDecimal>> score){
        //seminarService.scoreSeminar(score,seminarInfoVO);
        return new ResponseEntity(HttpStatus.OK);
    }



    /////////////////讨论课分数
    @RequestMapping(value ="/grade",method = RequestMethod.POST)
    public String teacherSeminarGrade(long klassSeminarId,Model model){
        model.addAttribute("seminarInfo",seminarService.getSeminarInfo(klassSeminarId));
        return "teacher_seminar_grade";
    }
}
