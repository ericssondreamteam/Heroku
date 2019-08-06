package hello;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@Controller
public class GreetingController {

    static String emailToRecipient, emailSubject, emailMessage;
    static final String emailFromRecipient = "karol.lasek@ericsson.com";


    public JavaMailSender mailSender;

    @GetMapping("/")
    public String getHome(){return "home";}

    @GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/mail")
    public String getMail(){
        return "mail";
    }

    @GetMapping("/emailForm")
    public String getMailForm(){
        return "emailForm";
    }

    //@Bean
    JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);

        mailSender.setUsername("karol.lasek@ericsson.com");
        mailSender.setPassword("Ericsson2019!");

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
    }

/*    public void sendMail(String to, String subject, String body)
    {
        mailSender = mailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }*/

    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request) {

        mailSender = mailSender();

        // Reading Email Form Input Parameters
        emailSubject = request.getParameter("subject");
        emailMessage = request.getParameter("message");
        emailToRecipient = request.getParameter("mailTo");

        // Logging The Email Form Parameters For Debugging Purpose
        System.out.println("\nReceipient?= " + emailToRecipient + ", Subject?= " + emailSubject + ", Message?= " + emailMessage + "\n");

        try{
            mailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {

                    MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    mimeMsgHelperObj.setTo(emailToRecipient);
                    mimeMsgHelperObj.setFrom(emailFromRecipient);
                    mimeMsgHelperObj.setText(emailMessage);
                    mimeMsgHelperObj.setSubject(emailSubject);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return "success";
    }
}