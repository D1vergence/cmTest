package cm.service;

import cm.dao.*;
import cm.entity.*;
import cm.vo.CourseKlassVO;
import cm.vo.TeamVO;
import cm.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class TeamService {
	@Autowired
	private TeamDAO teamDAO;
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private KlassDAO klassDAO;
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private StrategyDAO strategyDAO;
	@Autowired
	private AttendanceDAO attendanceDAO;

	public List<Team> findTeamsByCourseId(long courseId){
		return teamDAO.listByCourseId(courseId);
	}

	public List<Team> listStudentsNotInTeam(long courseId){
		return teamDAO.listByCourseId(courseId);
	}

	public TeamVO getMyTeam(long courseId, long studentId){
		Team team=teamDAO.getByCourseIdAndStudentId(courseId,studentId);
		return teamToTeamVO(team);
	}

	CourseKlassVO courseKlassToCourseKlassVO(long courseId, long KlassId)
	{
		Klass klass=klassDAO.getByKlassId(KlassId);
		Course course=courseDAO.getByCourseId(courseId);
		CourseKlassVO courseKlassVO=new CourseKlassVO();

		courseKlassVO.setCourseId(courseId);
		courseKlassVO.setCourseName(course.getCourseName());
		courseKlassVO.setKlassId(KlassId);
		courseKlassVO.setKlassName(klass.getGrade(),klass.getKlassSerial());
		return courseKlassVO;
	}

	TeamVO teamToTeamVO(Team team)
	{
		TeamVO teamVO=new TeamVO();
		teamVO.setCourseKlass(courseKlassToCourseKlassVO(team.getCourseId(),team.getKlassId()));
		teamVO.setLeader(userToUserVO(studentDAO.getByStudentId(team.getLeaderId())));
		teamVO.setTeamId(team.getId());
		teamVO.setTeamName(team.getTeamName());
		teamVO.setTeamNumber(klassDAO.getByKlassId(team.getKlassId()).getKlassSerial(),team.getTeamSerial());
		strategyDAO.judgeTeamValid(team);
		teamVO.setValid(team.getStatus());

		List<Student> studentList=team.getStudents();
		List<UserVO> userVOList=new LinkedList<UserVO>();
		for(int i=0;i<studentList.size();i++)
		{
			Student student=studentList.get(i);
			userVOList.add(userToUserVO(student));
		}
		teamVO.setMembers(userVOList);
		return teamVO;
	}

	public void quitTeam(long teamId,long studentId){
		teamDAO.deleteMemberByTeamIdAndStudentId(teamId,studentId);
	}

	public void createTeam(String teamName,long classId,List<String> studentNum){
		Team team=new Team();
		team.setKlassId(classId);
		team.setTeamName(teamName);
		teamDAO.createTeam(team);
		for(int i=0;i<studentNum.size();i++){
			Student tmp=studentDAO.getByNameOrAccount(studentNum.get(i));
			teamDAO.addTeamMemberByTeamIdAndStudentId(team.getId(),tmp.getId());
		}
	}

	public void deleteMember(long studentId,long teamId,List<String> studentNum){
		teamDAO.deleteMemberByTeamIdAndStudentId(teamId,studentId);
	}

	public void addMember(long studentId,long teamId,List<String> studentNum){
		teamDAO.addTeamMemberByTeamIdAndStudentId(teamId,studentId);
	}

	public void teamDisband(Long teamId,List<String> studentNum){
		List<Attendance> attendanceList=attendanceDAO.listByTeamId(teamId);
		for(int i=0;i<attendanceList.size();i++)
		attendanceDAO.deleteAttendanceByAttendanceId(attendanceList.get(i).getId());
		deleteTeam(teamId);
	}

	public UserVO searchStudent(String studentAccount){
		Student student=studentDAO.getByStudentAccount(studentAccount);
		return userToUserVO(student);
	}

	public UserVO userToUserVO(Student student)
	{
		UserVO userVO=new UserVO();
		userVO.setAccount(student.getAccount());
		userVO.setEmail(student.getEmail());
		userVO.setName(student.getStudentName());
		userVO.setRole("student");
		userVO.setId(student.getId());
		return userVO;

	}

    public List<Team> listTeamByCourseId(Long courseId) {
		List<Team> teams=teamDAO.listByCourseId(courseId);
		return teams;
    }

	public void deleteTeam(Long id) {
		List<Attendance> attendances=attendanceDAO.listByTeamId(id);
		for(int i=0;i<attendances.size();i++) {
			attendanceDAO.deleteAttendanceByAttendanceId(attendances.get(i).getId());
		}
		teamDAO.deleteTeamById(id);
	}

	public Team getByTeamId(Long teamId)
	{
		return teamDAO.getByTeamId(teamId);
	}
}
