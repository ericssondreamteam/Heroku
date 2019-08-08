package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController
{

    @RequestMapping("/emailForm")
    public String showForm(Model theModel) {

        // create a student object
        User theUser = new User();

        // add student object to the model
        theModel.addAttribute("student", theUser);

        return "emailForm";
    }

    @RequestMapping("/successFull")
    public String processForm(@ModelAttribute("student") User theUser)
    {

        // log the input data
        System.out.println(".....");

        return "success";
    }


}
