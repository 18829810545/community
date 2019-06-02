package com.example.demo.controller;

import com.example.demo.dto.AccessTokenDto;
import com.example.demo.dto.GithubUser;
import com.example.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client_id}")
    private String clientId;
    @Value("${github.client_secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;
   @GetMapping("/callback")
   public String callback(@RequestParam(name="code")String code,
                          @RequestParam(name="state")String state
                          ){
       AccessTokenDto accessTokenDto = new AccessTokenDto();
       accessTokenDto.setClient_id(clientId);
       accessTokenDto.setCode(code);
       accessTokenDto.setRedirect_uri(redirectUri);
       accessTokenDto.setState(state);
       accessTokenDto.setClient_secret(clientSecret);
       String accessToken = githubProvider.getAccessToken(accessTokenDto);
       GithubUser user = githubProvider.getUser(accessToken);
       System.out.println(user.getName());
       return "index";

  }

}
