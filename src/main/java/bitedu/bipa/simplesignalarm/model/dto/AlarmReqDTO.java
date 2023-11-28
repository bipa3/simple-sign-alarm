package bitedu.bipa.simplesignalarm.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmReqDTO {
    private LocalDateTime alarmDate;
    private String alarmCode;
    private int orgUserId;
    private int approvalDocId;
    private String positionName;
    private String gradeName;
    private String alarmContent;
    private int approverId;
}

