package uk.ac.bcu.invorchestrator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() throws Exception {
        return "Hi there!, Visit this path \"/swagger-ui/index.html\" for reference to the api docs.";
    }
}
