package cm.dao;

import cm.entity.LoginUser;
import cm.entity.Student;
import cm.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/22
 */
@Component
public class StudentDAO {

    @Autowired
    private StudentMapper studentMapper;

    public LoginUser getUserByStudentAccount(String userAccount){
        LoginUser result=studentMapper.getUserByStudentAccount(userAccount);
        if(result==null) {
            return null;
        }
        result.setUserRole("student");
        return result;
    }

    public Student getByStudentAccount(String account){
        return studentMapper.getByStudentAccount(account);
    }

    public List<Student> listAllStudent()
    {
        return studentMapper.listAllStudent();
    }

    public Student getByNameOrAccount(String nameOrAccount)
    {
        return studentMapper.getByNameOrAccount(nameOrAccount);
    }

    public Integer activateByStudentId(String passWord, String email, Long userId) {
        return studentMapper.activateByStudentId(passWord,email,userId);
    }

    public Student modifyStudent(Student student){
         int flag;
         try{
             flag=studentMapper.modifyStudent(student);
         }catch (Exception e){
             return null;
         }

         if(flag==0){
             return null;
         }

         return student;
    }


    public Student revertPassword(Long studentId){
        if(studentMapper.revertPassword(studentId)==1)
        {
            return studentMapper.getByStudentId(studentId);
        }
        return null;
    }

    public boolean deleteByStudentId(Long studentId)
    {
        if(studentMapper.deleteByStudentId(studentId)==1){
            return true;
        }
        return false;
    }

    public Student getByStudentId(Long studentId){
        return studentMapper.getByStudentId(studentId);
    }

    public Integer modifyPasswordByStudentId(String password,Long studentId){
        try {
            return studentMapper.modifyPasswordByStudentId(password, studentId);
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer modifyEmailByStudentId(String email,Long studentId){
        try {
            return studentMapper.modifyEmailByStudentId(email, studentId);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 无学生创建学生，有学生则加上id
     */
    public void createStudent(List<Student>studentList){
        for (Student student:studentList) {
            try {
                studentMapper.createStudent(student);
            } catch (Exception e) {
                Student student1=studentMapper.getByAccount(student.getAccount());
                student.setId(student1.getId());
            }
        }
    }

    public List<Student> listNoTeamStudentByCourseId(Long courseId){
        return studentMapper.listNoTeamStudentsByCourseId(courseId);
    }

    public List<Student> listNoTeamStudentByKlassId(Long klassId){
        return studentMapper.listNoTeamStudentsByKlassId(klassId);
    }

}
