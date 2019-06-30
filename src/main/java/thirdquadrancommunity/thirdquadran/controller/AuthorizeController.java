package thirdquadrancommunity.thirdquadran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thirdquadrancommunity.thirdquadran.dto.AccessTokenDTO;
import thirdquadrancommunity.thirdquadran.dto.GithubUser;
import thirdquadrancommunity.thirdquadran.mapper.UserMapper;
import thirdquadrancommunity.thirdquadran.model.User;
import thirdquadrancommunity.thirdquadran.provider.GithubProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null){
            User user = new User();
//            UUID：唯一通用识别码
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.insert(user);
            System.out.println("user信息写入数据库");
//            把token写入cookie，cookie在response里边，在形参里注入
            response.addCookie(new Cookie("token", token));
            System.out.println("将token写入cookie");
//            登录成功，写cookie和session，session从HttpServletRequest里拿到，将其写到方法里时，Spring会自动把上下文中的request
//            放到里边来使用
//            把user对象放到session里，相当于属于user的银行账户创建成功了
//            request.getSession().setAttribute("user", githubUser);
//            使用redirect重定向到页面上去，redirect返回的是路径
//            redirect告诉浏览器重新去请求那个地址
            return "redirect:/";
        }else{
//            登录失败，重新登录
            return "redirect:/";
        }
    }
}
