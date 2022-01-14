package knu.myhealthhub.portalnew.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MyConsent {
    @NonNull
    private String identifier;
    @NonNull
    private String title;
    @NonNull
    private String status;
    @NonNull
    private DataConsumer dataConsumer;
    @NonNull
    private ConsentPreference consentPreference;
    @NonNull
    private ReceivingInformation receivingInformation;
    //optional
    private String responseDate;
    private String lastUpdated;
    private List<String> referenceIdList;

    public MyConsent() {
        dataConsumer = new DataConsumer();
        consentPreference = new ConsentPreference();
        receivingInformation = new ReceivingInformation();
    }
}