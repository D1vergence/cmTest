package cm.dao;

import cm.entity.Klass;
import cm.entity.Student;
import cm.mapper.KlassMapper;
import cm.mapper.KlassRoundMapper;
import cm.mapper.KlassStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/22
 */
@Component
public class KlassDAO {
    @Autowired
    private KlassMapper klassMapper;

    @Autowired
    private KlassStudentMapper klassStudentMapper;

    @Autowired
    private KlassRoundMapper klassRoundMapper;

    public List<Klass> getByCourseId(Long courseId){
        return klassMapper.listByCourseId(courseId);
    }

    public Klass getByCourseIdAndStudentId(Long courseId, Long studentId){
        return klassMapper.getByCourseIdAndStudentId(courseId,studentId);
    }

    public int createByCourseId(Klass klass, Long courseId){
        try {
            return klassMapper.createByCourseId(klass, courseId);
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createKlassStudentListByStudentListAndKlassIdAndCourseId(List<Student>studentList, Long klassId, Long courseId){
        klassStudentMapper.deleteByKlassId(klassId);
        for (Student student:studentList) {
            klassStudentMapper.createByKlassIdAndStudentIdAndCourseId(klassId,
                    student.getId(),courseId);
        }
    }


    public Klass getByKlassId(Long klassId){
        return  klassMapper.getByKlassId(klassId);
    }

    public Klass getByKlassIdAndTeacherId(Long klassId, Long teacherId){
        return klassMapper.getByKlassIdAndTeacherId(klassId, teacherId);
    }

    public int deleteKlass(Long klassId){
        klassStudentMapper.deleteByKlassId(klassId);
        klassRoundMapper.deleteByKlassId(klassId);
        return klassMapper.deleteByKlassId(klassId);
    }

    public String getKlassNameByKlassId(Long klassId){
        Klass klass=klassMapper.getByKlassId(klassId);
        return String.valueOf(klass.getGrade())+"(" +String.valueOf(klass.getKlassSerial())+")";
    }

    public Long getKlassIdByCourseIdAndStudentId(Long courseId,Long studentId){
        return klassMapper.getKlassIdByCourseIdAndStudentId(courseId, studentId);
    }

    public Byte getEnrollNumberByRoundIdAndKlassId(Long klassId, Long roundId){
        return klassRoundMapper.getEnrollNumberByRoundIdAndKlassId(klassId,roundId);
    }
    //public Long getCourseIdByKlassId(Long klassId){
    //   return klassMapper.getCourseIdByKlassId(klassId);
    //}
    public int updateEnrollNumberByRoundIdAndKlassId(Byte enrollNumber, Long klassId, Long roundId){
        return klassRoundMapper.updateEnrollNumberByRoundIdAndKlassId(enrollNumber, klassId,roundId);
    }
    public Long getKlassIdByCourseIdAndKlassSerial(Long courseId, Long klassSerial){
        return klassMapper.getKlassIdByCourseIdAndKlassSerial(courseId,klassSerial);
    }
    public Long getCourseIdByKlassId(Long klassId){
        return klassMapper.getCourseIdByKlassId(klassId);
    }
}
