package bitedu.bipa.simplesignalarm.model.dto;

import lombok.Data;

@Data
public class UserApprovalDTO {
    private int alarmId;
    private String approvalDocTitle;
    private String userName;
    private int approvalCount;
}
