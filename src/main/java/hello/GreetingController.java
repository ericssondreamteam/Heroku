package hello;

import hello.model.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingController
{
    @Autowired
    private MailSenderService service;
    private User user = new User();

    @GetMapping("/")
    public String getHome()
    {
        return "home";
    }

    @GetMapping("/greeting")
    public String greeting()
    {
        return "greeting";
    }

    @GetMapping("/mail")
    public String getMail()
    {
        return "mail";
    }

    @GetMapping("/emailForm")
    public String getMailForm()
    {
        return "emailForm";
    }

    //ONLY FOR TEST
    @GetMapping("success")
    public String getSuccess()
    {
        return "success";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model)
    {
        // if(HtmlCondition.getCondition().equals("true"))
        service.print(model);
        return "login";
    }

    //to do : DELETE getPassword(); cannot tranfer it to service getLogin starts being null why?
    @RequestMapping(value = "getLogin", method = RequestMethod.POST)
    public String getLogin(HttpServletRequest request, Model model)
    {
        user = service.userLogin(request, model);
        if (MailSender.checkConnection(user).equals("login"))
            return "login";
        if (MailSender.checkConnection(user).equals("emailForm"))
            return "emailForm";
        else
            return null;
    }


    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request, @RequestParam("files") MultipartFile[] files, @RequestParam("images") MultipartFile[] images,
                                    @RequestParam(required = true) String mailTo, @RequestParam(required = true) String subject )
    {
        if (files.length > 10 || images.length > 10)
        {
            return "error";
        }
        // SMTP info
        String host = "smtp.office365.com";
        String port = "587";
        String mailFrom = user.getLogin();
        String password = user.getPassword();

        // message info
        request.getParameter("mailTo");
        request.getParameter("subject");

        //message body --> DO ZMIANY (jak rozwiazac zalaczanie obrazkow)
        StringBuffer body = service.getBody(request);

        // inline images --> DO ZMIANY (funkcja ktora jest wywolywana gdy jest dodany obrazek)
        Map<String, String> inlineImages = new HashMap<String, String>();
        service.images(images, body, inlineImages);
        body.append("</html>");
        ArrayList<String> paths = service.getFiles(files);

        try
        {
            MailSender.send(host, port, mailFrom, password, mailTo, subject, body.toString(), inlineImages, paths);
            System.out.println("Email sent.");
        }
        catch (Exception ex)
        {
            System.out.println("Could not send email.");
            ex.printStackTrace();
            return "error";
        }
        return "success";
    }
}