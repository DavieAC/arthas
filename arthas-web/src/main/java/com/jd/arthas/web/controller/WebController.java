package com.jd.arthas.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jd.arthas.common.constant.Constant;

@Controller
@RequestMapping("/")
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @ResponseBody
    @RequestMapping("/mainPage")
    public String mainPage() {
        return Constant.SUCCESS;
    }

}
