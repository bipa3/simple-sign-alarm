package bitedu.bipa.simplesignalarm.model.dto;
import lombok.Data;

@Data
public class ApprovalEvent {

    private int approvalDocId;
    private int receiverId;
    private String alarmCode;
    private int approverId;
}
