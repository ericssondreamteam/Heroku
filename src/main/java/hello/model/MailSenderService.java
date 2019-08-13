package hello.model;

import hello.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.SimpleAttributeSet;

@Service
public class MailSenderService
{
    User user;
    public MailSenderService()
    {
        User user = new User();
    }

    public void print(Model model)
    {
        System.out.println("\n\n**************LOGIN PAGE*************");
        model.addAttribute("valid", HtmlCondition.getCondition());
        System.out.println("----------------------------------------------->" + HtmlCondition.getCondition());
    }

    public void userLogin(HttpServletRequest request, Model model)
    {

    }

}
