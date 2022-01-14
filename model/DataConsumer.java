package knu.myhealthhub.portalnew.model;

import lombok.*;

@ToString
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataConsumer {
    @NonNull
    private String name;
    @NonNull
    private String institution;
    @NonNull
    private String requestDate;
    @NonNull
    private String expiredDate;
}
