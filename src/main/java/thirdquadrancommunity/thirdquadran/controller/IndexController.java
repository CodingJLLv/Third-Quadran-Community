package thirdquadrancommunity.thirdquadran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import thirdquadrancommunity.thirdquadran.mapper.UserMapper;
import thirdquadrancommunity.thirdquadran.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: Jinglei
 * @date: 2019-06-26
 * @description: 加上Controller注解后就自动识别扫描下边的类，当成spring的一个bean去管理,
 * 同时识别它为一个Controller，允许这个类接收前端的请求。
 */
@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
//    public String index(HttpServletRequest request){
//        return "index";
//    }
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    System.out.println("数据库中检测到user");
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
