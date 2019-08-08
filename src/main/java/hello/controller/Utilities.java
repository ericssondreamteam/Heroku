package hello.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class Utilities
{
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
