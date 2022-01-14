package knu.myhealthhub.portalnew.controller.individual;

import knu.myhealthhub.datamodels.ProfileBlock;
import knu.myhealthhub.portalnew.login_model.User;
import knu.myhealthhub.portalnew.model.MyConsent;
import knu.myhealthhub.portalnew.model.MyData;
import knu.myhealthhub.portalnew.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static knu.myhealthhub.portalnew.transactions.CDC04.*;
import static knu.myhealthhub.portalnew.transactions.REG02.parseDataResult;
import static knu.myhealthhub.portalnew.transactions.REG02.requestData;
import static knu.myhealthhub.settings.Configuration.*;
import static knu.myhealthhub.transactions.RestSender.createRest;
import static knu.myhealthhub.transactions.RestSender.getHeader;

@Controller
public class MyConsentController {
    public List<MyConsent> myConsentList = new ArrayList<>();
    public List<MyData> myDataList = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/myConsent")
    public String myConsent(Model model, @RequestParam(value = "selectedStatus", required = false) String selectedStatus, @RequestParam(value = "selectedConsent", required = false) String selectedConsent) {
        MyConsent myConsentDetail = new MyConsent();
        MyData myDataDetail = new MyData();

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsernameContaining(username);
        String uuid = user.getUuid();
        String requestDataResult = requestData(uuid);
        if (!requestDataResult.equalsIgnoreCase(FAILURE))
            myDataList = parseDataResult(requestDataResult);

        String resultConsentResult = requestConsentHistory(uuid);
        if (!resultConsentResult.equalsIgnoreCase(FAILURE)) {
            ProfileBlock profile = getProfile(uuid);
            myConsentList = parseConsentResult(resultConsentResult, profile);
        }
        model.addAttribute("selectedStatus", selectedStatus);
        model.addAttribute("myConsentList", filterConsent(selectedStatus, myConsentList));
        model.addAttribute("selectedConsent", selectedConsent);
        if (null != selectedConsent) {
            myConsentDetail = getMyConsentDetail(selectedConsent);
            StringTokenizer tokenizer = new StringTokenizer(selectedConsent, "|");
            String[] arr = new String[6];
            int idx = 0;
            while (tokenizer.hasMoreTokens()) {
                arr[idx] = tokenizer.nextToken();
                idx++;
            }
            String selectedData = String.format("%s|%s|%s", arr[2], arr[3], arr[4]);
            myDataDetail = getMyDataDetail(selectedData);
            System.out.println("uuid:" + uuid);
        }
        model.addAttribute("myConsentDetail", myConsentDetail);
        model.addAttribute("myDataDetail", myDataDetail);

        return "individual/menu-05-03";
    }

    @PostMapping("/myConsent/accept")
    public String acceptConsent(@RequestParam(value = "acceptId") String acceptId) {
        String url = URL_HEADER + CONSENT_ENDPOINT + URL_DEFAULT + CONSENT + "/" + acceptId + ACCEPT;
        createRest(url, HttpMethod.PATCH, getHeader(""), new JSONObject());
        System.out.println("accept" + url);
        return "redirect:/myConsent";
    }

    @PostMapping("/myConsent/reject")
    public String rejectConsent(@RequestParam(value = "rejectId") String rejectId) {
        String url = URL_HEADER + CONSENT_ENDPOINT + URL_DEFAULT + CONSENT + "/" + rejectId + REJECT;
        createRest(url, HttpMethod.PATCH, getHeader(""), new JSONObject());
        System.out.println("reject" + url);
        return "redirect:/myConsent";
    }

    @PostMapping("/myConsent/withdraw")
    public String withdrawConsent(@RequestParam(value = "expireId") String expireId) {
        String url = URL_HEADER + CONSENT_ENDPOINT + URL_DEFAULT + CONSENT + "/" + expireId + "/expire";
        System.out.println("withdraw" + url);
        createRest(url, HttpMethod.PATCH, getHeader(""), new JSONObject());
        return "redirect:/myConsent";
    }

    private MyConsent getMyConsentDetail(String identifier) {
        for (MyConsent consent : myConsentList) {
            if (identifier.equalsIgnoreCase(consent.getIdentifier())) {
                return consent;
            }
        }
        return null;
    }

    private MyData getMyDataDetail(String identifier) {
        System.out.println("entered");
        System.out.println("te:" + identifier);
        for (MyData data : myDataList) {
            System.out.println("re:" + data.getIdentifier());
            if (identifier.equalsIgnoreCase(data.getIdentifier())) {
                return data;
            }
        }
        return null;
    }

    private List<MyConsent> filterConsent(String status, List<MyConsent> myConsentList) {
        if ((null == status) || (status.equalsIgnoreCase("ALL"))) {
            return myConsentList;
        }
        List<MyConsent> filteredMyConsentList = new ArrayList<>();
        for (MyConsent myConsent : myConsentList) {
            if (myConsent.getStatus().equalsIgnoreCase(status)) {
                filteredMyConsentList.add(myConsent);
            }
        }
        return filteredMyConsentList;
    }
}