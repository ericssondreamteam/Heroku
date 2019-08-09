package hello;

import hello.model.HtmlCondition;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MailSender
{

    public static void send(String host, String port,
                            final String userName, final String password, String toAddress,
                            String subject, String htmlBody,
                            Map<String, String> mapInlineImages)
            throws AddressException, MessagingException
    {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
        properties.put("mail.debug", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator()
        {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        /*// creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds inline image attachments
        if (mapInlineImages != null && mapInlineImages.size() > 0)
        {
            Set<String> setImageID = mapInlineImages.keySet();

            for (String contentId : setImageID)
            {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);

                String imageFilePath = mapInlineImages.get(contentId);
                try
                {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(imagePart);
            }
        }

        msg.setContent(multipart);*/

        MimeBodyPart messageBodyPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        String file = "C:\\Users\\ELASKAR\\Desktop\\HTML\\Capture.PNG";
        String fileName = "Capture.PNG";
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);


        Transport.send(msg);
    }

    public static String checkConnection(User user){

        boolean check = false;
        //Java Version
        int port = 587;
        String host = "smtp.office365.com";
        String login = user.getLogin();
        String pwd = user.getPassword();

        try {
            Properties props = new Properties();
            // required for outlook
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.auth", "true");
            // or use getDefaultInstance instance if desired...
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, login, pwd);
            transport.close();
            System.out.println("---->checkConnection<---- Login and password correct.");
        }
        catch(AuthenticationFailedException e) {
            System.out.println("---->checkConnection<---- AuthenticationFailedException - for authentication failures");
            //e.printStackTrace();
            HtmlCondition.setCondition("true");
            return "login";
        }
        catch(MessagingException e) {
            System.out.println("---->checkConnection<---- for other failures");
           // e.printStackTrace();
            HtmlCondition.setCondition("true");
            return "login";
        }
        check = true;
        HtmlCondition.setCondition("false");
        return "emailForm";
    }

}
