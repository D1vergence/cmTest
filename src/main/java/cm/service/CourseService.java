package cm.service;

import cm.dao.*;
import cm.entity.*;
import cm.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CourseService {

	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private KlassDAO klassDAO;
	@Autowired
	private TeamDAO teamDAO;
	@Autowired
	private RoundScoreDAO roundScoreDAO;
	@Autowired
	private SeminarScoreDAO seminarScoreDAO;
	@Autowired
	private SeminarDAO seminarDAO;
	@Autowired
	private RoundDAO roundDAO;
	@Autowired
	private KlassSeminarDAO klassSeminarDAO;

	private CourseDetailVO course=new CourseDetailVO();
	@Autowired
	TeacherService teacherService;

	public List<Course> findCoursesByTeacherId(Long teacherId){
		return courseDAO.listByTeacherId(teacherId);
	}

	public Course findCourseById(Long courseId){
		return courseDAO.getByCourseId(courseId);
	}

	public List<Course> findCoursesByTeacher(Teacher teacher){
		return courseDAO.listByTeacherId(teacher.getId());
	}

	public boolean addCourse(CourseDetailVO course){
		Course course1=new Course();
		course1.setCourseName(course.getCourseName());
		course1.setId(course.getId());
		course1.setIntroduction(course.getIntroduction());
		course1.setKlasses(klassDAO.getByCourseId(course.getId()));
		course1.setPresentationPercentage(course.getPresentationPercentage());
		course1.setQuestionPercentage(course.getQuestionPercentage());
		course1.setReportPercentage(course.getReportPercentage());
		course1.setRounds(roundDAO.listByCourseId(course.getId()));
		course1.setTeamEndTime(Timestamp.valueOf(course.getTeamEndTime()));
		course1.setTeamStartTime(Timestamp.valueOf(course.getTeamStartTime()));
		//默认为主课程
		course1.setTeamMainCourseId(course.getId());

		courseDAO.createCourse(teacherService.t.getId(),course1);
		return true;
	}

	public boolean deleteCourseById(Long courseId){
		courseDAO.deleteByCourseId(courseId);
		return true;
	}

	public List<Map<Course, List<Klass>>> findCourseAndKlassByStudentId(Long studentId){
		List<Course> courses=courseDAO.listByStudentId(studentId);
		Map<Course,List<Klass>> ck=new HashMap<>();
		List<Map<Course, List<Klass>>> temp=new ArrayList<>();
		for(int i=0;i<courses.size();i++){
			List<Klass> klasses=klassDAO.getByCourseId(courses.get(i).getId());
			ck.put(courses.get(i),klasses);
			temp.add(ck);
		}
		return temp;
	}

	public Map<RoundScore,List<SeminarScore>> findScoreForStudent(Long courseId, Long klassId, Long studentId){
		Team team=teamDAO.getByCourseIdAndStudentId(courseId,studentId);
//		该学生该课程下（一个队）的所有讨论课成绩
		List<Round> rounds=roundDAO.listByCourseId(courseId);
		//List<RoundScore> roundScores = new LinkedList<RoundScore>();
		List<KlassSeminar> klassSeminars=klassSeminarDAO.listByKlassId(klassId);
		Map<RoundScore,List<SeminarScore>> maps=new HashMap<RoundScore,List<SeminarScore>>();
		//获得所有轮次的成绩
		for(int i=0;i<rounds.size();i++)
		{
			List<SeminarScore> seminarScores=new LinkedList<SeminarScore>();
			RoundScore roundScore=roundScoreDAO.getByRoundIdAndTeamId(rounds.get(i).getId(),team.getId());
			for(int j=0;j<klassSeminars.size();j++)
			{
				KlassSeminar klassSeminar=klassSeminars.get(j);
				//如果读取到的seminar的roundid等于当前round的roundid
				if(seminarDAO.getBySeminarId(klassSeminar.getSeminarId()).getRoundId()==rounds.get(i).getId()) {
					seminarScores.add(
							seminarScoreDAO.getByKlassSeminarIdAndTeamId(klassSeminar.getSeminarId(),team.getId())
					);
				}
			}
			maps.put(roundScore,seminarScores);
			//roundScores.add(roundScore);
		}
		return maps;
	}

	public Map<CourseVO, KlassVO> listCourseAndKlassByStudentId(Long studentId)
	{
        List<Course> courses=courseDAO.listByStudentId(studentId);
        Map<CourseVO,KlassVO> map=new HashMap<CourseVO,KlassVO>();
        for(int i=0;i<courses.size();i++)
        {
            Klass k=klassDAO.getByCourseIdAndStudentId(courses.get(i).getId(),studentId);
            KlassVO klassVO=new KlassVO();
            klassVO.setKlassId(k.getId());
            klassVO.setKlassName(k.getGrade(),k.getKlassSerial());
            CourseVO courseVO=new CourseVO();
            courseVO.setId(courses.get(i).getId());
            courseVO.setName(courses.get(i).getCourseName());

            map.put(courseVO,klassVO);
        }
        return map;
	}

	public CourseDetailVO getCourseById(Long courseId) {
		Course course = courseDAO.getByCourseId(courseId);
		CourseDetailVO courseDetailVO = new CourseDetailVO();
		courseDetailVO.setCourseName(course.getCourseName());
		courseDetailVO.setIntroduction(course.getIntroduction());
		courseDetailVO.setPresentationPercentage(course.getPresentationPercentage());
		courseDetailVO.setQuestionPercentage(course.getQuestionPercentage());
		courseDetailVO.setReportPercentage(course.getReportPercentage());
		courseDetailVO.setTeamStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(course.getTeamStartTime()));
		courseDetailVO.setTeamEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(course.getTeamEndTime()));
		return courseDetailVO;
	}

	//string是roundname
	public Map<String,SeminarScoreVO> listScoreForStudent(Long courseId, Long klassId, Long studentId) {
        //获取所有该班级下所有讨论课成绩，semimarid
        List<KlassSeminar> klassSeminars = klassSeminarDAO.listByKlassId(klassId);
        //获取小组id
        Team team = teamDAO.getByKlassIdAndStudentId(klassId, studentId);
        Map<String, SeminarScoreVO> map = new HashMap<String, SeminarScoreVO>();
        SeminarScore seminarScore;
        SeminarScoreVO seminarScoreVO=new SeminarScoreVO();
        //遍历查找所有讨论课，如果是该班级下的讨论课，就读一下该组的成绩
        for (int i = 0; i < klassSeminars.size(); i++) {
                //某班级讨论课下某个班的讨论课分数
                seminarScore = seminarScoreDAO.getByKlassSeminarIdAndTeamId(klassSeminars.get(i).getId(), team.getId());
            //通过courseid设置coursename
                seminarScoreVO.setCourseName(courseDAO.getByCourseId(courseId).getCourseName());
                seminarScoreVO.setKlassName(klassDAO.getKlassNameByKlassId(klassId));
                SimpleSeminarScoreVO simpleSeminarScoreVO=new SimpleSeminarScoreVO();
                //获得当前讨论课
                Seminar seminar=seminarDAO.getBySeminarId(klassSeminars.get(i).getSeminarId());
                //获得roundname/roundseries toString
                String s=roundDAO.getByRoundId(seminar.getRoundId()).getRoundSerial().toString();
                simpleSeminarScoreVO.setPresentationScore(seminarScore.getPresentationScore());
                simpleSeminarScoreVO.setQuestionScore(seminarScore.getQuestionScore());
                simpleSeminarScoreVO.setReportScore(seminarScore.getReportScore());
                simpleSeminarScoreVO.setTotalScore(seminarScore.getTotalScore());
                simpleSeminarScoreVO.setSeminarName(seminar.getSeminarName());
                seminarScoreVO.setSimpleSeminarScoreVO(simpleSeminarScoreVO);
                map.put(s,seminarScoreVO);
        }
    return map;
	}

    public List<Course> findCoursesByStudentId(Long studentId)
    {
        return courseDAO.listByStudentId(studentId);
    }

	public void setCourse(Long courseId)
	{
		CourseDetailVO courseDetailVO=new CourseDetailVO();
		Course course=courseDAO.getByCourseId(courseId);
		courseDetailVO.setCourseName(course.getCourseName());
		courseDetailVO.setTeamEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(course.getTeamEndTime()));
		courseDetailVO.setTeamStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(course.getTeamStartTime()));
		courseDetailVO.setReportPercentage(course.getReportPercentage());
		courseDetailVO.setQuestionPercentage(course.getQuestionPercentage());
		courseDetailVO.setPresentationPercentage(course.getPresentationPercentage());
		courseDetailVO.setIntroduction(course.getIntroduction());
		this.course = courseDetailVO;
	}

    public CourseDetailVO getCourse() {
        return course;
    }

	public List<Course> listCourseByTeacherId() {
		Teacher teacher=teacherService.t;
		List<Course> courses=courseDAO.listByTeacherId(teacher.getId());
		return courses;
	}

	public Map<String, List<SeminarScoreVO>> listScoreForTeacher(Long courseId) {
		//获取该课程下所有轮次
		//轮次-班级，总分-所有讨论课，分分
		List<Round> rounds=roundDAO.listByCourseId(courseId);
		Map<String, List<SeminarScoreVO>> map=new HashMap<String, List<SeminarScoreVO>> ();
		for(int i=0;i<rounds.size();i++)
		{
			Round round=rounds.get(i);
			String roundname=round.getRoundSerial().toString();

			//该课程下所有队伍
			List<Team> teams=teamDAO.listByCourseId(courseId);
			for(int j=0;j<teams.size();j++)
			{
				//获取当前课程所有班级
				Team team=teams.get(j);
				//根据轮次获得所有讨论课
				List<Seminar> seminars=seminarDAO.listByRoundId(round.getId());
				Klass klass=klassDAO.getByKlassId(team.getKlassId());
				List<SeminarScoreVO> seminarScoreVOS=new LinkedList<SeminarScoreVO>();
				for(int k=0;k<seminars.size();k++) {
					//依次获得讨论课下小组的成绩
					//获得该小组班级
					//很多重复的，不过都加一加吧
					SeminarScoreVO seminarScoreVO=new SeminarScoreVO();
					//1-1班级-队伍序号
					seminarScoreVO.setKlassName(klass.getKlassSerial().toString());
					Seminar seminar=seminars.get(k);
					SimpleSeminarScoreVO simpleSeminarScoreVO=new SimpleSeminarScoreVO();
					simpleSeminarScoreVO.setSeminarId(seminar.getId());
					simpleSeminarScoreVO.setSeminarSerial(seminar.getSeminarSerial());
					simpleSeminarScoreVO.setIntroduction(seminar.getIntroduction());
					simpleSeminarScoreVO.setSeminarName(seminar.getSeminarName());
					//这四个才是必不可少的
					SeminarScore seminarScore=seminarScoreDAO.getBySeminarIdAndKlassIdAndTeamId(seminar.getId(),klass.getId(),team.getId());
					simpleSeminarScoreVO.setPresentationScore(seminarScore.getPresentationScore());
					simpleSeminarScoreVO.setQuestionScore(seminarScore.getQuestionScore());
					simpleSeminarScoreVO.setReportScore(seminarScore.getPresentationScore());
					simpleSeminarScoreVO.setTotalScore(seminarScore.getTotalScore());
					seminarScoreVO.setSimpleSeminarScoreVO(simpleSeminarScoreVO);
					seminarScoreVOS.add(seminarScoreVO);
				}
				map.put(roundname,seminarScoreVOS);
			}

		}
return map;
	}

//	public void updateCourse(CourseVO course) {
//		Course course1=new Course();
//
//	}
}
