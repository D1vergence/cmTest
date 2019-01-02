package cm.service;

import cm.dao.AttendanceDAO;
import cm.entity.Attendance;
import cm.utils.FileUtil;
import cm.vo.WebSocketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceDAO attendanceDAO;

    public Attendance getByKlassSeminarIdAndKlassIdAndStudentId(Long klassSeminarId, Long klassId, Long studentId){
        return attendanceDAO.getByKlassSeminarIdAndKlassIdAndStudentId(klassSeminarId, klassId, studentId);
    }

    public Attendance getBySeminarIdAndKlassIdAndStudentId(Long seminarId, Long klassId, Long studentId){
        return attendanceDAO.getBySeminarIdAndKlassIdAndStudentId(seminarId, klassId, studentId);
    }

    public Attendance getByKlassSeminarIdAndTeamId(Long klassSeminarId,Long teamId){
        return attendanceDAO.getByKlassSeminarIdAndTeamId(klassSeminarId, teamId);
    }

    public Attendance getByAttendanceId(Long attendanceId){
        return attendanceDAO.getByAttendanceId(attendanceId);
    }

    public Attendance getByAttendanceIdAndStudentId(Long attendanceId,Long studentId){
        return attendanceDAO.getByAttendanceIdAndStudentId(attendanceId, studentId);
    }
    public int updatePptByAttendanceId(Long attendanceId,String pptName,String pptUrl){
        return attendanceDAO.updatePptByAttendanceId(attendanceId,pptName,pptUrl);
    }

    public int updateReportByAttendanceId(Long attendanceId,String reportName,String reportUrl){
        return attendanceDAO.updateReportByAttendanceId(attendanceId,reportName,reportUrl);
    }

    public void deleteAttendanceByAttendanceId(Long attendanceId){
        attendanceDAO.deleteAttendanceByAttendanceId(attendanceId);
    }

    public int addAttendance(Attendance attendance,Long klassSeminarId,Long teamId){
        return attendanceDAO.addAttendance(attendance,klassSeminarId,teamId);
    }

    public List<Attendance> listByKlassSeminarId(Long klassSeminarId){
        return attendanceDAO.listByKlassSeminarId(klassSeminarId);
    }

    public List<Attendance>listByTeamId(Long teamId){
        return attendanceDAO.listByTeamId(teamId);
    }

    public Long getTeamIdByAttendanceId(Long attendanceId){
        return  attendanceDAO.getTeamIdByAttendanceId(attendanceId);
    }

    public Long getKlassSeminarIdByAttendanceId(Long attendanceId){
        return attendanceDAO.getKlassSeminarIdByAttendanceId(attendanceId);
    }

    public String getReportUrlByAttendanceId(Long attendanceId){
        return  attendanceDAO.getReportUrlByAttendanceId(attendanceId);
    }

    public String getPptUrlByAttendanceId(Long attendanceId){
        return attendanceDAO.getPptUrlByAttendanceId(attendanceId);
    }

    public int startAttendance(Long attendanceId){
        return attendanceDAO.startAttendance(attendanceId);
    }

    public int endAttendance(Long attendanceId){
        return  attendanceDAO.endAttendance(attendanceId);
    }

    public List<Attendance> listByRoundIdAndTeamId(Long roundId,Long teamId){
        return attendanceDAO.listByRoundIdAndTeamId(roundId,teamId);
    }

    public Attendance getByKlassSeminarIdAndTeamOrder(Long klassSeminarId, int i) {
        return attendanceDAO.getByKlassSeminarIdAndTeamOrder(klassSeminarId,i);
    }

}
