package cm.service;

import cm.dao.KlassDAO;
import cm.dao.StudentDAO;
import cm.entity.LoginUser;
import cm.entity.Student;
import cm.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {


    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private KlassDAO klassDAO;

    public UserVO getUserVOByAccount(String account) {
        Student s=studentDAO.getByStudentAccount(account);
        UserVO student=new UserVO();
        student.setId(s.getId());
        student.setAccount(s.getAccount());
        student.setEmail(s.getEmail());
        student.setName(s.getStudentName());
        student.setRole("student");
        return student;
    }


    public Student getStudentByAccount(String account){
        return studentDAO.getByStudentAccount(account);
    }

    public boolean vertify(String account, String password){
        Student tmp=studentDAO.getByStudentAccount(account);
        if(tmp.getPassword().equals(password))
            return true;
        else
            return false;
    }

    public boolean modifyStudentEmail(String email,UserVO student){
        studentDAO.modifyEmailByStudentId(email,student.getId());
        return true;
    }

    public boolean modifyStudentPwd(String password,UserVO student){
        studentDAO.modifyEmailByStudentId(password,student.getId());
        return true;
    }

    public List<Student> findAllStudents(){
        return studentDAO.listAllStudent();
    }

    public void modifyStudent(long id,String account,String studentName,String email){
        Student student=new Student();
        student.setEmail(email);
        student.setStudentName(studentName);
        student.setAccount(account);
        student.setId(id);
        studentDAO.modifyStudent(student);
    }

    public boolean active(String password,String password1,String email)
    {
        if(password.equals(password1))
        return true;
        else
            return false;
    }

    public Byte getIs_active(UserVO s)
    {
        return s.getIsActive();
    }

    public Student getByStudentId(Long studentId) {
        return studentDAO.getByStudentId(studentId);
    }

    public List<Student> getStudentNotInTeam(Long courseId,Long studentId) {
        Long klassId=klassDAO.getKlassIdByCourseIdAndKlassSerial(courseId,studentId);
        return studentDAO.listNoTeamStudentByKlassId(klassId);//班级id
    }
}
