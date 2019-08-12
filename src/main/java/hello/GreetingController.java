package hello;

import hello.model.HtmlCondition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingController
{
    private User user;

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
    public String getLoginPage(Model model){
       // if(HtmlCondition.getCondition().equals("true"))
        System.out.println("\n\n**************LOGIN PAGE*************");
        model.addAttribute("valid",HtmlCondition.getCondition());
        System.out.println("----------------------------------------------->"+HtmlCondition.getCondition());
        return "login";
    }

    //to do : DELETE getPassword();
    @RequestMapping(value = "getLogin", method = RequestMethod.POST)
    public String getLogin(HttpServletRequest request, Model model){
        user = new User(request.getParameter("login"), request.getParameter("password"));
        System.out.println("getLogin -----> LOGIN: " + user.getLogin() + " PASSWORD: " + user.getPassword());
        HtmlCondition.setCondition("true");
        model.addAttribute("valid",HtmlCondition.getCondition());
        System.out.println("GET---------------------------------------------->"+HtmlCondition.getCondition());

        if(MailSender.checkConnection(user).equals("login"))
            return "login";
        if(MailSender.checkConnection(user).equals("emailForm"))
            return "emailForm";
        else
            return null;
    }

    //NEW VERSION (fast as fuck)XD
    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public String sendEmailToClient(HttpServletRequest request, @RequestParam("files") MultipartFile[] files,@RequestParam("images") MultipartFile[] images)
    {
        // SMTP info
        String host = "smtp.office365.com";
        String port = "587";
        /*String mailFrom = "ericssonStart@outlook.com";
        String password = "qwerty12345";*/
        String mailFrom = user.getLogin();
        String password = user.getPassword();

        // message info
        String mailTo = request.getParameter("mailTo");
        String subject = request.getParameter("subject");


        //message body --> DO ZMIANY (jak rozwiazac zalaczanie obrazkow)
        StringBuffer body
                = new StringBuffer("<html>This message contains two inline images.<br>");

        //new added text 7.08.2019
        //String bodyFromForm = request.getParameter("message");
        
        String bodyFromForm = request.getParameter("editor1");
        System.out.println(bodyFromForm);
        
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
        ArrayList<String> imagesPath = new ArrayList<>();
        if(((images != null) && (images.length > 0) && (!images.equals("")))){
            for(MultipartFile file :images)
            {
                File newFile2;
                String pathAndFilename;

                try
                {
                    newFile2 = convert(file);
                    System.out.println("-----------------> FILE PATH" + newFile2.getAbsolutePath());
                    pathAndFilename = newFile2.getAbsolutePath();
                    imagesPath.add(pathAndFilename);

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        inlineImages.put("image1", imagesPath.get(0));
        inlineImages.put("image2", imagesPath.get(1));


        //add files
        //@RequestParam("files") MultipartFile[] files
        ArrayList<String> paths = new ArrayList<>();

        if(((files != null) && (files.length > 0) && (!files.equals("")))){
            for(MultipartFile file :files)
            {
                //Path fileNameAndPath= Paths.get(file.getOriginalFilename());
                //paths.add(fileNameAndPath.toString());

                File newFile;
                String pathAndFilename;

                try
                {
                    newFile = convert(file);
                    System.out.println("-----------------> FILE PATH" + newFile.getAbsolutePath());
                    pathAndFilename = newFile.getAbsolutePath();
                    paths.add(pathAndFilename);

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                System.out.println("----------> MULTIPART PATH" + file.getOriginalFilename());
            }
        }


        try
        {
            MailSender.send(host, port, mailFrom, password, mailTo,
                    subject, body.toString(), inlineImages,paths);
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

    public static File convert(MultipartFile file) throws IOException
    {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}