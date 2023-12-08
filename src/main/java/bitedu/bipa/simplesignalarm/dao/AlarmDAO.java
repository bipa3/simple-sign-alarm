package bitedu.bipa.simplesignalarm.dao;

import bitedu.bipa.simplesignalarm.mapper.AlarmMapper;
import bitedu.bipa.simplesignalarm.model.dto.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlarmDAO {

    private final AlarmMapper alarmMapper;

    public AlarmDAO(AlarmMapper mapper) {
        this.alarmMapper = mapper;
    }

    public int insertAlarm(AlarmReqDTO alarmReqDTO) {
        return alarmMapper.insertAlarm(alarmReqDTO);
    }

    public String selectDefaultMessage(String alarmCode) {
        return alarmMapper.selectDefaultMessage(alarmCode);
    }

    public List<AlarmDTO> selectAlarm(int orgUserId){
        return alarmMapper.alarmSelect(orgUserId);
    }

    // 문서명 들고오기
    public UserApprovalDTO selectApprovalDocTitle(String alarmDate, int orgUserId, String alarmCode, int approvalDocId){
        return alarmMapper.selectApprovalDocTitle(alarmDate, orgUserId, alarmCode, approvalDocId);
    }

    // 실시간 결재자 들고오기
    public String selectApprovalUser(int alarmId){
        return alarmMapper.selectApprovalUser(alarmId);
    }

    // 안읽은 알림 총 갯수
    public int alarmCount(int orgUserId){
        return alarmMapper.AlarmCount(orgUserId);
    }

    // 알림 읽음 처리
    public boolean updateConfirmationStatus(int alarmId){
        return alarmMapper.updateConfirmationStatus(alarmId);
    }

    // 알림 삭제하고 삽입
    public AlarmDeleteDTO selectAlarmOn(int alarmId){
        return alarmMapper.selectAlarmOn(alarmId);
    }
    public boolean insertAlarmDelete (AlarmDeleteDTO alarmDeleteDTO){
        return alarmMapper.insertAlarmDelete(alarmDeleteDTO);
    }
    public void deleteAlarm(int alarmId) {
        alarmMapper.deleteAlarm(alarmId);
    }

    // 마지막 알림 아이디
    public int maxAlarmId(int orgUserId){
        return alarmMapper.maxAlarmId(orgUserId);
    }
    // 유실된 알림 재전송
    public List<AlarmReqDTO> selectFailAlarm(int orgUserId, int alarmId){
        return alarmMapper.selectFailAlarm(orgUserId, alarmId);
    }
}
