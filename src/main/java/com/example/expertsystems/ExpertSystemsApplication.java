package com.example.expertsystems;

import knowledgBase.TestTipperJava;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class ExpertSystemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpertSystemsApplication.class, args);
    }

    @GetMapping("/es")
    @ResponseBody
    public String getSolution(
            @RequestParam("respiratoryRate") String respiratoryRate,
            @RequestParam("upperArterialPressure") String upperArterialPressure,
            @RequestParam("lowerArterialPressure") String lowerArterialPressure,
            @RequestParam("age") String age
    ){
        return Double.toString(TestTipperJava.solution(Integer.parseInt(respiratoryRate), Integer.parseInt(upperArterialPressure), Integer.parseInt(lowerArterialPressure), Integer.parseInt(age)));
    }
    @GetMapping("/e")
    @ResponseBody
    public String getSolutio(){
        return "d";
    }
}
