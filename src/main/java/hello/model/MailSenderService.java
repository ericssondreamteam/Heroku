package hello.model;

import hello.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.SimpleAttributeSet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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

    public void images(@RequestParam("images") MultipartFile[] images, StringBuffer body, Map<String, String> inlineImages)
    {
        int imagesCounter = 0;
        if ((images != null) && (images.length > 0) && (!images.equals("")))
        {
            for (MultipartFile file : images)
            {
                File newFile2;
                String pathAndFilename;
                try
                {
                    imagesCounter++;
                    newFile2 = convert(file);
                    pathAndFilename = newFile2.getAbsolutePath();

                    inlineImages.put("image" + imagesCounter, pathAndFilename);
                    body.append("<img src=\"cid:image" + imagesCounter + "\" width=\"30%\" height=\"30%\" /><br>");

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public User userLogin(HttpServletRequest request, Model model)
    {
        user = new User(request.getParameter("login"), request.getParameter("password"));
        System.out.println("getLogin -----> LOGIN: " + user.getLogin() + " PASSWORD: " + user.getPassword());
        HtmlCondition.setCondition("true");
        model.addAttribute("valid", HtmlCondition.getCondition());
        System.out.println("GET---------------------------------------------->" + HtmlCondition.getCondition());
        return user;
    }

    public ArrayList<String> getFiles(@RequestParam("files") MultipartFile[] files)
    {
        ArrayList<String> paths = new ArrayList<>();

        if (((files != null) && (files.length > 0) && (!files.equals(""))))
        {
            for (MultipartFile file : files)
            {
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
        return paths;
    }


    public StringBuffer getBody(HttpServletRequest request)
    {
        StringBuffer body = new StringBuffer("<html>This message contains two inline images.<br>");
        String bodyFromForm = request.getParameter("editor1");
        String firstAnswer = request.getParameter("field1");
        String secondAnswer = request.getParameter("field2");
        String thirdAnswer = request.getParameter("field3");
        String fourthAnswer = request.getParameter("field4");
        System.out.println(bodyFromForm);
        body.append(bodyFromForm + "<br>" + "Pole1" + "<br>" + firstAnswer + "<br><br>" + "Pole2"+ "<br>"+
                secondAnswer + "<br><br>" + "Pole3" + "<br>" + thirdAnswer + "<br><BR>" + "Pole4"
                + "<br>" + fourthAnswer + "<br><br>");
        //System.out.println(body);
        return body;
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
