package hello;

import hello.controller.FileUploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        new File(FileUploadController.uploadDirectory).mkdir();
        SpringApplication.run(Application.class, args);
    }
}