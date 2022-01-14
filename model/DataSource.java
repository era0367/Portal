package knu.myhealthhub.portalnew.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataSource {
    @NonNull
    private String institution;
    @NonNull
    private String institutionType;
    @NonNull
    private String author;
}
