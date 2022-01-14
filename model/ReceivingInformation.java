package knu.myhealthhub.portalnew.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceivingInformation {
    @NonNull
    private String method;
    @NonNull
    private String period;
}
