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
