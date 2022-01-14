package knu.myhealthhub.portalnew.model;

import knu.myhealthhub.datamodels.Target;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MyData {
    @NonNull
    private String identifier;
    @NonNull
    private String title;
    @NonNull
    private DataSource dataSource;
    @NonNull
    private DataInformation dataInformation;
    @NonNull
    private Target target;

    public MyData() {
        dataSource = new DataSource();
        dataInformation = new DataInformation();
        target = new Target();
    }
}