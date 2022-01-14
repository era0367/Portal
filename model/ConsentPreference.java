package knu.myhealthhub.portalnew.model;

import knu.myhealthhub.datamodels.InformationSetting;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsentPreference {
    @NonNull
    private String type;
    @NonNull
    private String withdrawal;
    @NonNull
    private String extension;
    @NonNull
    private InformationSetting informationSetting;
    //optional
    private String delegator;
}