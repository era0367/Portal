package knu.myhealthhub.portalnew.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataInformation {
    @NonNull
    private String type;
    @NonNull
    private String format;
    @NonNull
    private String language;
    @NonNull
    private String creationDate;
    //optional
    private String comments;
}
