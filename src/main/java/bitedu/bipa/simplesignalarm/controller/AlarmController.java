package bitedu.bipa.simplesignalarm.controller;

import bitedu.bipa.simplesignalarm.model.dto.AlarmDTO;
import bitedu.bipa.simplesignalarm.model.dto.AlarmResDTO;
import bitedu.bipa.simplesignalarm.service.AlarmService;
import bitedu.bipa.simplesignalarm.service.RedisService;
import bitedu.bipa.simplesignalarm.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

//    @Autowired
//    private RedisService redisService;

//    public int getOrgUserId(HttpServletRequest request){
////        int authorityCode = (SessionUtils.getAttribute("authorityCode") != null) ? (int) SessionUtils.getAttribute("authorityCode") : 3;
//        List<String> cookieHeaderValues = Collections.list(request.getHeaders("Cookie"))
//                .stream()
//                .flatMap(cookieHeader -> Arrays.stream(cookieHeader.split(";")))
//                .map(String::trim)
//                .collect(Collectors.toList());
//
//        String JSESSIONID = "";
//        for(String value: cookieHeaderValues) {
//            if(value.contains("JSESSIONID")) {
//                 JSESSIONID = value.substring(value.indexOf("=")+1);
//            }
//        }
//        Object orgUserId1 = redisService.getValueFromHash(JSESSIONID, "orgUserId");
//        System.out.println("JSESSIONID" + orgUserId1);
//        return Integer.parseInt((String) orgUserId1);
//    }

    @PostMapping("/createNewAlarm")
    public void createNewAlarm(@RequestBody AlarmResDTO alarmResDTO){
        alarmService.createNewAlarm(alarmResDTO.getApprovalDocId(), alarmResDTO.getReceiverId(), alarmResDTO.getAlarmCode());
    }

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

}


