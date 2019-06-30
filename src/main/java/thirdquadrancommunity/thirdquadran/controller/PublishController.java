package thirdquadrancommunity.thirdquadran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thirdquadrancommunity.thirdquadran.mapper.QuestionMapper;
import thirdquadrancommunity.thirdquadran.mapper.UserMapper;
import thirdquadrancommunity.thirdquadran.model.Question;
import thirdquadrancommunity.thirdquadran.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: Jinglei
 * @date: 2019-06-26
 * @description:
 */
@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){


        // 为了提交抛异常后页面上依然呈现title，description，tag的信息，放到model中保存起来，model能在页面上获取到
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);



        if(title == null || title == ""){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }




        // 从cookie里获得user信息
        User user = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        // 如果在服务端api接口级别传递到页面，需要写到model里传递过去
        if(user == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }



        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtModified());


        // 如果没有异常则重定向首页
        questionMapper.create(question);
        return "redirect:/";
    }
}
