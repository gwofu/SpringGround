package com.devry.mobile.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devry.mobile.models.GpaModel;
import com.devry.mobile.services.BannerService;

@RestController
public class SampleController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/api/sample")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @Autowired
    private BannerService bannerService;
    
    @RequestMapping("/api/gpa")
    public GpaModel getGpa(@RequestParam(value="dsi", defaultValue="0") String dsi) {
        return bannerService.getGpa(dsi);
    }

}

final class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
