package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class GreetingController
{

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

    //NEW VERSION (fast as fuck)XD
    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request)
    {
        // SMTP info
        String host = "smtp.office365.com";
        String port = "587";
        String mailFrom = "ericssonStart@outlook.com";
        String password = "qwerty12345";

        // message info
        String mailTo = request.getParameter("mailTo");
        String subject = request.getParameter("subject");


        //message body --> DO ZMIANY (jak rozwiazac zalaczanie obrazkow)
        StringBuffer body = new StringBuffer("<html>This message contains two inline images.<br>");

        //new added text 7.08.2019
        String bodyFromForm = request.getParameter("message");
        body.append(bodyFromForm + "<br>");

        body.append("First Image:<br>");
        body.append("<img src=\"cid:image1\" width=\"30%\" height=\"30%\" /><br>");
        body.append("The second one:<br>");
        body.append("<img src=\"cid:image2\" width=\"15%\" height=\"15%\" /><br>");
        body.append("End of message.");
        body.append("</html>");

        // inline images --> DO ZMIANY (funkcja ktora jest wywolywana gdy jest dodany obrazek)
        Map<String, String> inlineImages = new HashMap<String, String>();

        String workingDirectory = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";
        System.out.println("-------------->" + workingDirectory);


        inlineImages.put("image1", workingDirectory + "cat.jpg");
        inlineImages.put("image2", workingDirectory + "rabbit.jpg");


        try
        {
            MailSender.send(host, port, mailFrom, password, mailTo, subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        }
        catch (Exception ex)
        {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
        return "success";
    }

}