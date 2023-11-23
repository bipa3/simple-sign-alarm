package bitedu.bipa.simplesignalarm.model.dto;

import lombok.Data;

@Data
public class AlarmFailDTO {
    private int alarmId;
    private String alarmDate;
    private String alarmCode;
    private int orgUserId;
    private int approvalDocId;
    private String positionName;
    private String gradeName;
    private String alarmContent;
}
