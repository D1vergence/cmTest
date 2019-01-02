package cm.controller;

import cm.service.AttendanceService;
import cm.vo.SeminarInfoVO;
import cm.vo.WebSocketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@MessageMapping("/cm/seminar/")
public class AttendanceController {

    @Autowired
    private static AttendanceService attendanceService;

    /*@MessageMapping("/nextAttendance")
    @SendTo("/topic/broadcast")
    public WebSocketVO nextAttendance(@RequestBody SeminarInfoVO seminarInfoVO) {
        WebSocketVO webSocketVO=attendanceService.nextAttendance(seminarInfoVO.getSeminarId(),
                seminarInfoVO.getKlassId());
        return webSocketVO;
    }*/
}
