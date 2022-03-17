package com.example.expertsystems;

import knowledgBase.TestTipperJava;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solution")
public class Controller {
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("")
    @ResponseBody
    public String getSolution(@RequestParam("respiratoryRate") String respiratoryRate,
                              @RequestParam("upperArterialPressure") String upperArterialPressure,
                              @RequestParam("lowerArterialPressure") String lowerArterialPressure,
                              @RequestParam("age") String age)
    {
        return Double.toString(TestTipperJava.solution(Integer.parseInt(respiratoryRate), Integer.parseInt(upperArterialPressure), Integer.parseInt(lowerArterialPressure), Integer.parseInt(age)));
    }
}

