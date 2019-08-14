package hello;

import hello.model.HtmlCondition;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class MailSender
{

    public static void send(String host, String port,
                            final String userName, final String password, String toAddress,
                            String subject, String htmlBody,
                            Map<String, String> mapInlineImages, ArrayList<String> paths)
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
        MimeBodyPart messageBodyPart;
        // creates a new e-mail message
        Message msg = getMessage(userName, toAddress, subject, session);
        Multipart multipart = getMultipart(htmlBody);


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
        if (paths.size() != 0)
        {
            //petla ktora przechodzi po tablicy String

            for (String s : paths)
            {
                messageBodyPart = new MimeBodyPart();//todo jak było poza for to nie działało
                //System.out.println("TESTTTTTTTTTTTTTTTT:              "+s);
                String file = s;
                String[] path = s.split(Pattern.quote("\\"));
                String fileName = path[path.length - 1];
                //System.out.println("TESTTTTTTTTTTTTTTTT plik:              "+path[path.length-1]);
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);

                //i na koniec dodaje pliczek
                multipart.addBodyPart(messageBodyPart);
            }

        }
        msg.setContent(multipart);
        Transport.send(msg);
        deleteAllFile(mapInlineImages,paths);
    }

    public static Multipart getMultipart(String htmlBody) throws MessagingException
    {
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        return multipart;
    }

    public static Message getMessage(String userName, String toAddress, String subject, Session session) throws MessagingException
    {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        return msg;
    }

    public static String checkConnection(User user)
    {
        boolean check = false;
        //Java Version
        int port = 25;
        String host = "smtp.internal.ericsson.com";
        String login = user.getLogin();
        String pwd = user.getPassword();

        try
        {
            Properties props = new Properties();
            // required for outlook
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            // or use getDefaultInstance instance if desired...
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, login, pwd);
            transport.close();
            System.out.println("---->checkConnection<---- Login and password correct.");
        } catch (AuthenticationFailedException e)
        {
            System.out.println("---->checkConnection<---- AuthenticationFailedException - for authentication failures");
            //e.printStackTrace();
            HtmlCondition.setCondition("true");
            return "login";
        } catch (MessagingException e)
        {
            System.out.println("---->checkConnection<---- for other failures");
            // e.printStackTrace();
            HtmlCondition.setCondition("true");
            return "login";
        }
        check = true;
        HtmlCondition.setCondition("false");
        return "emailForm";
    }
    static void deleteAllFile(Map<String, String> mapInlineImages, ArrayList<String> paths)
    {
        System.out.println(System.getProperty("user.dir"));
        Set<String> setImageID = mapInlineImages.keySet();

        for (String contentId : setImageID)
        {
            String imageFilePath = mapInlineImages.get(contentId);
            if(imageFilePath.startsWith(System.getProperty("user.dir")))
                deleteFile(imageFilePath);
        }
        for(String s:paths)
            if(s.startsWith(System.getProperty("user.dir")))
                deleteFile(s);
    }
    static void deleteFile(String path)
    {
        File file = new File(path);

        if(file.delete())
        {
            System.out.println("File deleted successfully "+path);
        }
        else
        {
            System.out.println("Failed to delete the file "+path);
        }
    }
}
