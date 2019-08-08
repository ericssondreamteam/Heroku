package hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController
{
    public static String uploadDirectory = System.getProperty("user.dir") + "\\uploads";

    @RequestMapping("/uploadHome")
    public String uploadPage(Model model)
    {
        return "uploadView";
    }

    @RequestMapping("/upload")
    public String upload(Model model, @RequestParam("files")MultipartFile[] files)
    {
        StringBuilder fileNames =new StringBuilder();
        for(MultipartFile file :files)
        {
            Path fileNameAndPath= Paths.get(file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            try
            {
                System.out.println("-----------------------------> \t"+uploadDirectory+file.getName());
                Files.write(Paths.get(uploadDirectory+"\\"+file.getName()),file.getBytes());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        model.addAttribute("msg", "Successfully uploaded files "+fileNames.toString());
        return "uploadStatusView";
    }

}
