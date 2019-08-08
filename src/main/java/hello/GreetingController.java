package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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


    //NEW VERSION (fast as fuck)XD
    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request,  Model model)
    {
        // SMTP info
        String host = "smtp.office365.com";
        String port = "587";
        String mailFrom = "ericssonStart@outlook.com";
        String password = "qwerty12345";

        // message info
        String mailTo = request.getParameter("mailTo");
        String subject = request.getParameter("subject");


        model.addAttribute("message", mailTo);
        model.addAttribute("message", subject);


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


    @RequestMapping("/success")
    public String getSucces(@RequestParam("mailTo") String mailTo, @RequestParam("subject") String subject , Model model)
    {
        // add message to the model
        model.addAttribute("message", mailTo);
        model.addAttribute("message", subject);

        return "success";
    }


}