package com.example.demo.controller;

import com.example.demo.User.User;
import com.example.demo.dto.AccessTokenDto;
import com.example.demo.dto.GithubUser;
import com.example.demo.mapper.UserMapper;
import com.example.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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
    @Autowired
    private UserMapper userMapper;
   @GetMapping("/callback")
   public <request> String callback(@RequestParam(name="code")String code,
                                    @RequestParam(name="state")String state,
                                    HttpServletResponse response){
       AccessTokenDto accessTokenDto = new AccessTokenDto();
       accessTokenDto.setClient_id(clientId);
       accessTokenDto.setClient_secret(clientSecret);
       accessTokenDto.setCode(code);
       accessTokenDto.setRedirect_uri(redirectUri);
       accessTokenDto.setState(state);
       String accessToken = githubProvider.getAccessToken(accessTokenDto);
       GithubUser githubUser = githubProvider.getUser(accessToken);
       if(githubUser!=null){
           User user = new User();
           String token = UUID.randomUUID().toString();
           user.setToken(token);
           user.setName(githubUser.getName());
           user.setAccountId(String.valueOf(githubUser.getId()));
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
           response.addCookie(new Cookie("token",token));
           return "redirect:/";
       }else{
            //登录失败，重新登录
           return "redirect:/";
       }


  }

}
