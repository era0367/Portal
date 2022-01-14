package knu.myhealthhub.portalnew.controller.individual;

import knu.myhealthhub.portalnew.login_model.User;
import knu.myhealthhub.portalnew.model.MyData;
import knu.myhealthhub.portalnew.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static knu.myhealthhub.portalnew.transactions.REG02.parseDataResult;
import static knu.myhealthhub.portalnew.transactions.REG02.requestData;
import static knu.myhealthhub.settings.Configuration.FAILURE;

@Controller
public class MyDataController {
    public List<MyData> myDataList = new ArrayList<>();
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/myData")
    public String myData(Model model, @RequestParam(value = "selectedType", required = false) String selectedType, @RequestParam(value = "checkedData", required = false) String checkedData) {
        MyData myDataDetail = new MyData();

        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userRepository.findByUsernameContaining(username);
        String uuid = user.getUuid();

        String result = requestData(uuid);
        if (!result.equalsIgnoreCase(FAILURE)) myDataList = parseDataResult(result);

        model.addAttribute("selectedType", selectedType);
        model.addAttribute("myDataList", filterData(selectedType, myDataList));
        model.addAttribute("checkedData", checkedData);
        if (null != checkedData) {
            myDataDetail = getMyDataDetail(checkedData);
        }
        model.addAttribute("myDataDetail", myDataDetail);

        return "individual/menu-05-02";
    }

    private MyData getMyDataDetail(String identifier) {
        for (MyData data : myDataList) {
            if (identifier.equalsIgnoreCase(data.getIdentifier())) {
                return data;
            }
        }
        return null;
    }

    private List<MyData> filterData(String type, List<MyData> myDataList) {
        if ((null == type) || (type.equalsIgnoreCase("ALL"))) {
            return myDataList;
        }
        List<MyData> filterdMyDataList = new ArrayList<>();
        for (MyData myData : myDataList) {
            if (myData.getDataInformation().getType().equalsIgnoreCase(type)) {
                filterdMyDataList.add(myData);
            }
        }
        return filterdMyDataList;
    }
}
