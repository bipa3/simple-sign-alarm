package bitedu.bipa.simplesignalarm.mapper;

import bitedu.bipa.simplesignalarm.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface AlarmMapper {

    int insertAlarm(AlarmReqDTO dto);

    String selectDefaultMessage(String alarmCode);

    // 전체 알림 들고오기
    List<AlarmDTO> alarmSelect(int orgUserId);

    // 문서명
    UserApprovalDTO selectApprovalDocTitle(@Param("alarmDate") String alarmDate, @Param("orgUserId") int orgUSerId,
                                           @Param("alarmCode") String alarmCode, @Param("approvalDocId") int approvalDocId);

    // 실시간 결재자
    String selectApprovalUser(int approvalDocId);

    // 안읽은 알림 총 갯수
    int AlarmCount(int orgUserId);

    // 알림 읽음 처리
    boolean updateConfirmationStatus(int alarmId);

    // 알림 삭제하고 삽입
    AlarmDeleteDTO selectAlarmOn(int alarmId);
    boolean insertAlarmDelete(AlarmDeleteDTO alarmDeleteDTO);
    void deleteAlarm(int alarmId);

    // 실패한 알림 테이블에 넣기
    void failAlarmInsert(AlarmFailDTO alarmFailDTO);

    List<AlarmReqDTO> failAlarmSelect();

    // 실패 알림 아이디
    int failId(@Param("alarmDate") String alarmDate, @Param("orgUserId") int orgUserId,
                    @Param("alarmCode") String alarmCode, @Param("approvalDocId") int approvalDocId);

    // 실패 알림 삭제
    void deleteFailAlarm(int failId);

    // 실패 알림 전체 삭제
    void deleteFailAlarmAll(int orgUserId);
}
