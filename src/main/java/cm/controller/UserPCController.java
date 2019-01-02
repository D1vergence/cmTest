package cm.controller;

import cm.service.StudentService;
import cm.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cm/pc")
public class UserPCController {
/*
    StudentService studentService=new StudentService();
    TeacherService teacherService=new TeacherService();

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String pcLogin(){
        return "pcLogin";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String pcloginSubmit(String account,String password) {
        if (account.length() == 11) {
            if (studentService.vertify(account, password))
                return "redirect:/cm/pc/student";
        } else {
            if(teacherService.vertify(account,password))
                return "redirect:/cm/pc/teacher";
        }
        return "pcLogin";
    }

    */
}
