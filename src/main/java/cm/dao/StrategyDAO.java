package cm.dao;

import cm.entity.*;
import cm.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Yunfeng Huang
 * @Description:
 * @Date: Created in 2018/12/24
 */
@Component
public class StrategyDAO {
    private static final String TEAM_AND_STRATEGY = "TeamAndStrategy";
    private static final String TEAM_OR_STRATEGY = "TeamOrStrategy";
    private static final String CONFLICT_COURSE_STRATEGY = "ConflictCourseStrategy";
    private static final String MEMBER_LIMIT_STRATEGY = "MemberLimitStrategy";
    private static final String COURSE_MEMBER_LIMIT_STRATEGY= "CourseMemberLimitStrategy";
    private static final Integer MAX_MEMBER=6;


    @Autowired
    private ConflictCourseStrategyMapper conflictCourseStrategyMapper;

    @Autowired
    private MemberLimitStrategyMapper memberLimitStrategyMapper;

    @Autowired
    private CourseMemberLimitStrategyMapper courseMemberLimitStrategyMapper;

    @Autowired
    private TeamStrategyMapper teamStrategyMapper;

    @Autowired
    private TeamAndStrategyMapper teamAndStrategyMapper;

    @Autowired
    private TeamOrStrategyMapper teamOrStrategyMapper;

    /**
     * 总判断入口
     */
    public void judgeTeamValid(Team team){
        if (team.getStudents().size()>=MAX_MEMBER){
            team.setStatus((byte)0);
            return;
        }
        List<TeamStrategy> teamStrategyList=teamStrategyMapper.listByCourseId(team.getCourseId());
        for (TeamStrategy teamStrategy:teamStrategyList) {
            if (false== judgeByTotalStrategy(teamStrategy.getStrategyName(),teamStrategy.getStrategyId(),team)) {
                team.setStatus((byte)0);
                return;
            }
        }
        team.setStatus((byte)1);
    }
    /**
     * 根据名字判断此记录符不符合实际
     */
    private Boolean judgeByTotalStrategy(String strategyName, Long strategyId, Team team){
        switch (strategyName){
            case TEAM_AND_STRATEGY :
                if (!judgeByTeamAndStrategy(strategyId,team)) {
                    return false;
                }
                break;
            case TEAM_OR_STRATEGY :
                if (!judgeByTeamOrStrategy(strategyId,team)) {
                    return false;
                }
                break;
            case CONFLICT_COURSE_STRATEGY:
                if (!judgeByConflictCourseStrategy(strategyId,team)) {
                    return false;
                }
                break;
            case MEMBER_LIMIT_STRATEGY :
                if (!judgeByMemberLimitStrategy(strategyId,team)) {
                    return false;
                }
                break;
            case COURSE_MEMBER_LIMIT_STRATEGY :
                if (!judgeByCourseMemberLimitStrategy(strategyId,team)) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private Boolean judgeByTeamAndStrategy(Long strategyId, Team team){
        List<TeamAndStrategy>teamAndStrategyList=teamAndStrategyMapper.listByStrategyId(strategyId);
        for (TeamAndStrategy teamAndStrategy:teamAndStrategyList) {
            if (false== judgeByTotalStrategy(teamAndStrategy.getStrategyName(),teamAndStrategy.getStrategyId(),team)) {
                return false;
            }
        }
        return true;
    }

    private Boolean judgeByTeamOrStrategy(Long strategyId, Team team){
        List<TeamOrStrategy>teamOrStrategyList=teamOrStrategyMapper.listByStrategyId(strategyId);
        for (TeamOrStrategy teamOrStrategy:teamOrStrategyList) {
            if (true== judgeByTotalStrategy(teamOrStrategy.getStrategyName(),teamOrStrategy.getStrategyId(),team)) {
                return true;
            }
        }
        return false;
    }

    private Boolean judgeByConflictCourseStrategy(Long strategyId, Team team){
        List<ConflictCourseStrategy>conflictCourseStrategyList=conflictCourseStrategyMapper.listConflictCourseByStrategyId(strategyId);
        Integer klassNum=0,klassTypeNum=0;
        for (ConflictCourseStrategy conflictCourseStrategy:conflictCourseStrategyList) {
            for (Student student:team.getStudents()) {
                for (Long courseId:student.getCourseIdList()) {
                    if (courseId.equals(conflictCourseStrategy.getCourseId())) {
                        klassNum++;
                    }
                }
            }
            if (klassNum>0){
                klassTypeNum++;
                klassNum=0;
            }
            if (klassTypeNum >=2) {
                return false;
            }
        }
        return true;
    }

    private Boolean judgeByMemberLimitStrategy(Long strategyId, Team team){
        MemberLimitStrategy memberLimitStrategy=memberLimitStrategyMapper.getByMemberLimitStrategyId(strategyId);
        Integer teamSize=team.getStudents().size();
        if (null != memberLimitStrategy.getMinMember()&&teamSize<memberLimitStrategy.getMinMember()) {
            return false;
        }
        if (null != memberLimitStrategy.getMaxMember()&&teamSize>memberLimitStrategy.getMaxMember()) {
            return false;
        }
        return true;
    }
    private Boolean judgeByCourseMemberLimitStrategy(Long strategyId, Team team){
        CourseMemberLimitStrategy courseMemberLimitStrategy=courseMemberLimitStrategyMapper.getByStrategyId(strategyId);
        Integer count=0;
        for (Student student:team.getStudents()) {
            for (Long courseId:student.getCourseIdList()) {
                if (courseId.equals(courseMemberLimitStrategy.getCourseId())) {
                    count++;
                }
            }
        }
        if (null != courseMemberLimitStrategy.getMinMember()&&count<courseMemberLimitStrategy.getMinMember()) {
            return false;
        }
        if (null != courseMemberLimitStrategy.getMaxMember()&&count>courseMemberLimitStrategy.getMaxMember()) {
            return false;
        }
        return true;
    }

    public int createCourseMemberLimitStrategy(CourseMemberLimitStrategy courseMemberLimitStrategy){
        return  courseMemberLimitStrategyMapper.createCourseMemberLimitStrategy(courseMemberLimitStrategy);
    }

//    public int createMemberLimitStrategy(MemberLimitStrategy memberLimitStrategy){
//        return  memberLimitStrategyMapper.createMemberLimitStrategy(memberLimitStrategy);
//    }
//
//    public int createTeamAndStrategy(MemberLimitStrategy memberLimitStrategy){
//        return  memberLimitStrategyMapper.createMemberLimitStrategy(memberLimitStrategy);
//    }
//
//    public int createTeamOrStrategy(MemberLimitStrategy memberLimitStrategy){
//        return  memberLimitStrategyMapper.createMemberLimitStrategy(memberLimitStrategy);
//    }
}
