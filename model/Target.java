package knu.myhealthhub.portalnew.model;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Target {
    private Boolean no;
    private Boolean all;
    private Boolean family;
    private Boolean medicalInstitution;
    private Boolean publicInstitution;
    private Boolean researchInstitution;
    private Boolean insuranceCompany;
    private Boolean privateCompany;
    private String targetName;
    
    public void setAll(Boolean bool) {
        if(bool) {
            this.no = false;
            this.family = true;
            this.medicalInstitution = true;
            this.publicInstitution = true;
            this.researchInstitution = true;
            this.insuranceCompany = true;
            this.privateCompany = true;
        }
    }
    public void setNo(Boolean bool) {
        if(bool) {
            this.all = false;
            this.family = false;
            this.medicalInstitution = false;
            this.publicInstitution = false;
            this.researchInstitution = false;
            this.insuranceCompany = false;
            this.privateCompany = false;
        }
    }
}
