package bitedu.bipa.simplesignalarm.service;

import bitedu.bipa.simplesignalarm.dao.AlarmDAO;
import bitedu.bipa.simplesignalarm.dao.CommonDAO;
import bitedu.bipa.simplesignalarm.model.dto.*;
import bitedu.bipa.simplesignalarm.validation.CustomErrorCode;
import bitedu.bipa.simplesignalarm.validation.RestApiException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlarmService {

    private AlarmDAO alarmDAO;
    private CommonDAO commonDAO;
    private SimpMessagingTemplate messagingTemplate;

    public AlarmService(AlarmDAO alarmDAO, CommonDAO commonDAO, SimpMessagingTemplate messagingTemplate) {
        this.alarmDAO = alarmDAO;
        this.commonDAO = commonDAO;
        this.messagingTemplate = messagingTemplate;
    }


    @KafkaListener(topics = "alarmTopic", groupId = "group-id-alarm")
    public void getAlarm(@Payload ApprovalEvent alarmResDTO) {
        System.out.println("alarmTopic 응답");
        this.createNewAlarm(alarmResDTO.getApprovalDocId(), alarmResDTO.getReceiverId(),alarmResDTO.getAlarmCode(),alarmResDTO.getApproverId());
    }
    @Transactional
    public void createNewAlarm(int approvalDocId, int orgUserId, String alarmCode, int approverId) {
        PositionAndGradeDTO positionAndGradeDTO = commonDAO.getPositionAndGrade(orgUserId);
        AlarmReqDTO alarmReqDTO = new AlarmReqDTO();
        alarmReqDTO.setAlarmCode(alarmCode);
        alarmReqDTO.setAlarmDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        alarmReqDTO.setOrgUserId(orgUserId);
        alarmReqDTO.setApproverId(approverId);
        alarmReqDTO.setGradeName(positionAndGradeDTO.getGradeName());
        alarmReqDTO.setPositionName(positionAndGradeDTO.getPositionName());
        alarmReqDTO.setApprovalDocId(approvalDocId);

        String defaultMessage = alarmDAO.selectDefaultMessage(alarmCode);
        //조건에 따른 알람 메세지 넣기(추후 수정)
        alarmReqDTO.setAlarmContent(defaultMessage);
        int affectedCount = alarmDAO.insertAlarm(alarmReqDTO);
        if(affectedCount > 0){
            try {
                this.sendMeassge(alarmReqDTO);
            }catch (Exception e) {
                System.out.println(e);
            }
        }else{
            throw  new RestApiException(CustomErrorCode.ALARM_INSERT_FAIL);
        }
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void sendMeassge(AlarmReqDTO alarmReqDTO){
        LocalDateTime alarmDate = alarmReqDTO.getAlarmDate();
        alarmDate = alarmDate.truncatedTo(ChronoUnit.SECONDS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = alarmDate.format(formatter);
        UserApprovalDTO userApprovalDto = alarmDAO.selectApprovalDocTitle(formattedDate, alarmReqDTO.getOrgUserId(), alarmReqDTO.getAlarmCode(), alarmReqDTO.getApprovalDocId());
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setAlarmCode(alarmReqDTO.getAlarmCode());
        alarmDTO.setAlarmDate(formattedDate);
        alarmDTO.setAlarmContent(alarmReqDTO.getAlarmContent());
        alarmDTO.setApprovalDocId(alarmReqDTO.getApprovalDocId());
        alarmDTO.setApprovalDocTitle(userApprovalDto.getApprovalDocTitle());
        alarmDTO.setAlarmId(userApprovalDto.getAlarmId());

        // 실시간 결재자를 들고옴
        String userName = alarmDAO.selectApprovalUser(alarmDTO.getAlarmId());
        if(userName != null) {
            alarmDTO.setUserName(userName);
        }

        messagingTemplate.convertAndSend("/topic/alarm/" + alarmReqDTO.getOrgUserId(), alarmDTO);
    }

    // 마지막 알림 아이디
    public int maxAlarmId(int orgUserId){
        return alarmDAO.maxAlarmId(orgUserId);
    }

    // 유실된 알림 재전송
    public void selectFailAlarm(int orgUserId, int alarmId){
        List<AlarmReqDTO> fail = alarmDAO.selectFailAlarm(orgUserId, alarmId);
        for(AlarmReqDTO alarmReqDTO : fail){
           this.sendMeassge(alarmReqDTO);
        }
    }

    // 전체 알림을 들고오는 부분
    public List<AlarmDTO> selectAlarm(int orgUserId) {

        List<AlarmDTO> alarmDTOList = alarmDAO.selectAlarm(orgUserId);

        for(AlarmDTO alarmDTO : alarmDTOList) {
            int alarmId = alarmDTO.getAlarmId();
            String userName = alarmDAO.selectApprovalUser(alarmId);

            if(userName != null) {
                alarmDTO.setUserName(userName);
            }
        }
        return alarmDTOList;
    }

    // 알림의 갯수를 구함
    public int alarmCount(int orgUserId){
        return alarmDAO.alarmCount(orgUserId);
    }

    // 알림의 읽음으로 변경
    public boolean updateConfirmationStatus(int alarmId){
        return alarmDAO.updateConfirmationStatus(alarmId);
    }

    // 알림 삭제 삽입
    public void deleteAlarm(int alarmId){
        AlarmDeleteDTO alarmDeleteDTO = alarmDAO.selectAlarmOn(alarmId);
        boolean flag = alarmDAO.insertAlarmDelete(alarmDeleteDTO);
        if(flag){
            alarmDAO.deleteAlarm(alarmDeleteDTO.getAlarmId());
        }
    }
}
