package com.phincon.laza.controller;


import com.phincon.laza.model.dto.response.DataResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwitterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterController.class);

    @RequestMapping("/twitterCallback")
    public void twitterCallBack(
            @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier,
            @RequestParam(value = "denied", required = false) String denied,
            HttpServletResponse response, Model model){
//        if (denied != null){
//            return ""
//        }

    }
}
