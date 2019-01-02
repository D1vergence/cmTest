package cm.controller;

import cm.entity.Course;
import cm.entity.Student;
import cm.entity.Teacher;
import cm.service.*;
//import cm.service.UserService;
import cm.vo.UserVO;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class UserController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;

	public static UserVO userVO;

	//登录
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String Login() {
		return "userlogin";
	}

	//登录提交表单
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String LoginSubmit(String account, String password) throws IOException {
		if (account.length() == 11) {
			if (studentService.vertify(account, password)) {
				userVO=studentService.getUserVOByAccount(account);
				return "redirect:/cm/student/index";
			} else {
				if (teacherService.vertify(account, password)) {
					teacherService.getUserVOByAccount(account);
					return "redirect:/cm/teacher/index";
				}
			}
			return "userlogin";
		}
		else return "redirect:/cm/teacher/index";
	}
}
