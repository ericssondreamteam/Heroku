package hello;

import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
public class GreetingController {

    /*static String emailToRecipient, emailSubject, emailMessage;
    static final String emailFromRecipient = "ericssonStart@outlook.com";


    public JavaMailSender mailSender;*/

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/mail")
    public String getMail() {
        return "mail";
    }

    @GetMapping("/emailForm")
    public String getMailForm() {
        return "emailForm";
    }

    //ONLY FOR TEST
    @GetMapping("success")
    public String getSuccess() {
        return "success";
    }

    /*JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);
        mailSender.setUsername("ericssonStart@outlook.com");
        mailSender.setPassword("qwerty12345");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//add
        props.put("mail.smtp.socketFactory.port", "465");                           //add
        props.put("mail.smtp.starttls.enable", "true");                             //add
        props.put("mail.smtp.quitwait", "false");
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }*/

    //OLD FUNCTION

    /*@RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request) {

        mailSender = mailSender();

        // Reading Email Form Input Parameters
        emailSubject = request.getParameter("subject");
        emailMessage = request.getParameter("message");
        emailToRecipient = request.getParameter("mailTo");

        // Logging The Email Form Parameters For Debugging Purpose
        System.out.println("\nReceipient?= " + emailToRecipient + ", Subject?= " + emailSubject + ", Message?= " + emailMessage + "\n");

        try {
            mailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    mimeMsgHelperObj.setTo(emailToRecipient);
                    mimeMsgHelperObj.setFrom(emailFromRecipient);
                    mimeMsgHelperObj.setText(emailMessage);
                    mimeMsgHelperObj.setSubject(emailSubject);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }*/


    //NEW VERSION (fast as fuck)XD
    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request) {
        // SMTP info
        String host = "smtp.office365.com";
        String port = "587";
        String mailFrom = "ericssonStart@outlook.com";
        String password = "qwerty12345";

        // message info
        String mailTo = request.getParameter("mailTo");
        String subject = request.getParameter("subject");
        StringBuffer body
                = new StringBuffer("<html>This message contains two inline images.<br>");

        //new added text 7.08.2019
        String bodyFromForm = request.getParameter("message");
        body.append(bodyFromForm + "<br>");

        body.append("First Image:<br>");
        body.append("<img src=\"cid:image1\" width=\"30%\" height=\"30%\" /><br>");
        body.append("The second one:<br>");
        body.append("<img src=\"cid:image2\" width=\"15%\" height=\"15%\" /><br>");
        body.append("End of message.");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
        inlineImages.put("image1", "C:/Users/ELASKAR/Downloads/MailWitam/cat.jpg");
        inlineImages.put("image2", "C:/Users/ELASKAR/Downloads/MailWitam/rabbit.jpg");

        try {
            MailSender.send(host, port, mailFrom, password, mailTo,
                    subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
        return "success";
    }

}