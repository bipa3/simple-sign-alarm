package bitedu.bipa.simplesignalarm.controller;

import bitedu.bipa.simplesignalarm.model.dto.AlarmDTO;
import bitedu.bipa.simplesignalarm.model.dto.AlarmMessage;
import bitedu.bipa.simplesignalarm.model.dto.AlarmResDTO;
import bitedu.bipa.simplesignalarm.service.AlarmService;
import bitedu.bipa.simplesignalarm.service.RedisService;
import bitedu.bipa.simplesignalarm.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;


//    @PostMapping("/createNewAlarm")
//    public void createNewAlarm(@RequestBody AlarmResDTO alarmResDTO){
//        alarmService.createNewAlarm(alarmResDTO.getApprovalDocId(), alarmResDTO.getReceiverId(), alarmResDTO.getAlarmCode(), alarmResDTO.ge);
//    }

    @GetMapping("/")
    public List<AlarmDTO> getAlarm(){
        int orgUserId = (int) SessionUtils.getAttribute("orgUserId");
        List<AlarmDTO> alarmDTO = alarmService.selectAlarm(orgUserId);
        return alarmDTO;
    }

    @GetMapping("/count")
    public int alarmCount(HttpSession session){
        int orgUserId = (int) session.getAttribute("orgUserId");
        return alarmService.alarmCount(orgUserId);
    }

    @PutMapping("/update/{alarmId}")
    public boolean confirmationStatusUpdate(@PathVariable int alarmId) {
        return alarmService.updateConfirmationStatus(alarmId);
    }

    @DeleteMapping("/delete/{alarmId}")
    public void deleteAlarm(@PathVariable int alarmId){
        alarmService.deleteAlarm(alarmId);
    }

    @MessageMapping("/alarmMessage")
    public void alarmMessage(@Payload AlarmMessage alarmMessage) {
        int lastAlarmId = alarmMessage.getLastAlarmId();
        int orgUserId = alarmMessage.getOrgUserId();
        int maxAlarmId = alarmService.maxAlarmId(orgUserId);
        if(lastAlarmId < maxAlarmId){
            alarmService.selectFailAlarm(orgUserId, lastAlarmId);
        }
    }
}


