package knu.myhealthhub.portalnew.model;

import knu.myhealthhub.datamodels.Target;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTargetList {
    private List<Target> myTargets;

    public void addTarget(Target target) {
        if(null == myTargets) {
            initMyTargets();
        }
        this.myTargets.add(target);
    }
    public void initMyTargets() {
        this.myTargets = new ArrayList<>();
    }
}
