package com.efida.sports.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wnagyan on 2018/7/18.
 */
@Controller
public class H5Controller {
    @RequestMapping("H5Login")
    public String about() {
        return "pages/login";
    }
    @RequestMapping("gradeRanking")
    public String gradeRanking() {
        return "pages/gradeRanking";
    }
    @RequestMapping("myGrade")
    public String myGrade() {
        return "pages/myGrade";
    }
    @RequestMapping("massageCenter")
    public String massageCenter() {
        return "pages/massageCenter";
    }
    @RequestMapping("competitionResults")
    public String aboutUs() {
        return "pages/competitionResults";
    }
    @RequestMapping("medals")
    public String medals() {
        return "pages/medals";
    }
    @RequestMapping("myGradeDetail")
    public String myGradeDetail() {
        return "pages/myGradeDetail";
    }
    @RequestMapping("newDetailSuccess")
    public String newDetailSuccess() {
        return "pages/newDetailSuccess";
    }
}
