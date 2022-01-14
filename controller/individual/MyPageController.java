package knu.myhealthhub.portalnew.controller.individual;

import knu.myhealthhub.datamodels.DataType;
import knu.myhealthhub.datamodels.Preference;
import knu.myhealthhub.datamodels.ProfileBlock;
import knu.myhealthhub.datamodels.Target;
import knu.myhealthhub.enums.RECEIVING_METHOD;
import knu.myhealthhub.enums.RECEIVING_PERIOD;
import knu.myhealthhub.portalnew.login_model.User;
import knu.myhealthhub.portalnew.model.MyTargetList;
import knu.myhealthhub.portalnew.repository.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static knu.myhealthhub.common.JsonUtility.*;
import static knu.myhealthhub.portalnew.transactions.CDC03.updateProfile;
import static knu.myhealthhub.portalnew.transactions.CDC04.getProfile;
import static knu.myhealthhub.settings.Configuration.SUCCESS;


@Controller
public class MyPageController {
    private String uuid;
    private ProfileBlock profile;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/myPage")
    public String myPage() {
        return "individual/menu-05-01";
    }

    @GetMapping("/myPage/preference")
    public String myPreference(Model model) {
        MyTargetList myAutoList = new MyTargetList();
        MyTargetList myTargetList = new MyTargetList();

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsernameContaining(username);
        uuid = user.getUuid();
        profile = getProfile(uuid);
        Preference preference = profile.getPreference();

        String decodedAuto = new String(Base64.decodeBase64(preference.getAutomaticConsent()));
        JSONObject automaticConsentJson = toJsonObject(decodedAuto);
        DataType automaticConsent = toJavaObject(automaticConsentJson, DataType.class);
        automaticConsent.getDefault_data().setTargetName("DEFAULT");
        automaticConsent.getPhysical_data().setTargetName("PHYSICAL");
        automaticConsent.getMental_data().setTargetName("MENTAL");
        automaticConsent.getMedication_data().setTargetName("MEDICATION");
        automaticConsent.getGenomic_data().setTargetName("GENOMIC");
        automaticConsent.getPatient_generated_data().setTargetName("PGHD");
        myAutoList.addTarget(automaticConsent.getDefault_data());
        myAutoList.addTarget(automaticConsent.getPhysical_data());
        myAutoList.addTarget(automaticConsent.getMental_data());
        myAutoList.addTarget(automaticConsent.getMedication_data());
        myAutoList.addTarget(automaticConsent.getGenomic_data());
        myAutoList.addTarget(automaticConsent.getPatient_generated_data());

        String decodedTarget = new String(Base64.decodeBase64(preference.getTarget()));
        JSONObject targetJson = toJsonObject(decodedTarget);
        DataType target = toJavaObject(targetJson, DataType.class);
        target.getDefault_data().setTargetName("DEFAULT");
        target.getPhysical_data().setTargetName("PHYSICAL");
        target.getMental_data().setTargetName("MENTAL");
        target.getMedication_data().setTargetName("MEDICATION");
        target.getGenomic_data().setTargetName("GENOMIC");
        target.getPatient_generated_data().setTargetName("PGHD");
        myTargetList.addTarget(target.getDefault_data());
        myTargetList.addTarget(target.getPhysical_data());
        myTargetList.addTarget(target.getMental_data());
        myTargetList.addTarget(target.getMedication_data());
        myTargetList.addTarget(target.getGenomic_data());
        myTargetList.addTarget(target.getPatient_generated_data());

        model.addAttribute("myAutoList", myAutoList);
        model.addAttribute("myTargetList", myTargetList);
        model.addAttribute("myReceivingMethod", preference.getMethod().toString());
        model.addAttribute("myReceivingPeriod", preference.getPeriod().toString());

        return "individual/menu-05-01-02";
    }

    @PostMapping("/myPage/preference/dataTable")
    public String setPreferenceData(@ModelAttribute("myTargetList") MyTargetList myTargetList) {
        List<Target> targetList = myTargetList.getMyTargets();
        DataType target = getDataTypeFromTarget(targetList);
        String targetString = getEncodedDataType(target);
        profile.getPreference().setTarget(targetString);
        Preference preference = profile.getPreference();
        preference.setTarget(targetString);
        String result = updateProfile(profile);
        if (result.equalsIgnoreCase(SUCCESS))
            return "redirect:/myPage/preference/";
        return null;
    }

    @PostMapping("/myPage/preference/consentTable")
    public String setPreferenceConsent(@ModelAttribute("myAutoList") MyTargetList myTargetList) {
        List<Target> targetList = myTargetList.getMyTargets();
        DataType target = getDataTypeFromTarget(targetList);
        String targetString = getEncodedDataType(target);
        profile.getPreference().setTarget(targetString);
        Preference preference = profile.getPreference();
        preference.setAutomaticConsent(targetString);
        String result = updateProfile(profile);
        if (result.equalsIgnoreCase(SUCCESS))
            return "redirect:/myPage/preference/";
        return null;
    }

    @PostMapping("/myPage/preference/receivingInfo")
    public String setPreferenceReceiving(@RequestParam("receivingMethod") String receivingMethod, @RequestParam("receivingPeriod") String receivingPeriod) {
        Preference preference = profile.getPreference();
        if (!receivingMethod.equals("선택안함")) {
            if (receivingMethod.equals("이메일"))
                preference.setMethod(RECEIVING_METHOD.EMAIL);
            if (receivingMethod.equals("전화"))
                preference.setMethod(RECEIVING_METHOD.PHONE);
            if (receivingMethod.equals("문자"))
                preference.setMethod(RECEIVING_METHOD.MESSAGE);
        }
        if (!receivingPeriod.equals("없음")) {
            if (receivingPeriod.equals("매번"))
                preference.setPeriod(RECEIVING_PERIOD.EACH_TIME);
            if (receivingPeriod.equals("한달에 한번"))
                preference.setPeriod(RECEIVING_PERIOD.ONCE_FOR_EVERY_MONTH);
            if (receivingPeriod.equals("세달에 한번"))
                preference.setPeriod(RECEIVING_PERIOD.ONCE_FOR_EVERY_THREE_MONTHS);
            if (receivingPeriod.equals("6개월에 한번"))
                preference.setPeriod(RECEIVING_PERIOD.ONCE_FOR_EVERY_SIX_MONTHS);
            if (receivingPeriod.equals("1년에 한번"))
                preference.setPeriod(RECEIVING_PERIOD.ONCE_FOR_EVERY_YEAR);
        }
        String result = updateProfile(profile);
        if (result.equalsIgnoreCase(SUCCESS))
            return "redirect:/myPage/preference/";
        return null;
    }

    private String getEncodedDataType(DataType dataType) {
        String jsonString = toJsonObjectFromJavaObject(dataType);
        return new String(Base64.encodeBase64(jsonString.getBytes()));
    }

    private DataType getDataTypeFromTarget(List<Target> targetList) {
        DataType dataType = new DataType();
        Target defaultData = findTargetByName(targetList, "DEFAULT");
        Target physicalData = findTargetByName(targetList, "PHYSICAL");
        Target mentalData = findTargetByName(targetList, "MENTAL");
        Target medicationData = findTargetByName(targetList, "MEDICATION");
        Target genomicData = findTargetByName(targetList, "GENOMIC");
        Target patientGeneratedData = findTargetByName(targetList, "PGHD");
        dataType.setDefault_data(defaultData);
        dataType.setPhysical_data(physicalData);
        dataType.setMental_data(mentalData);
        dataType.setMedication_data(medicationData);
        dataType.setGenomic_data(genomicData);
        dataType.setPatient_generated_data(patientGeneratedData);
        return dataType;
    }

    private Target findTargetByName(List<Target> targetList, String name) {
        Target target = new Target();
        for (int i = 0; i < targetList.size(); i++) {
            if (name.equalsIgnoreCase(targetList.get(i).getTargetName())) {
                target = targetList.get(i);
                break;
            }
        }
        target.setTargetName(null);
        return target;
    }
}
