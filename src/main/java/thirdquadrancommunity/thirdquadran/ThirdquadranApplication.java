package thirdquadrancommunity.thirdquadran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: Jinglei
 * @date: 2019-06-27
 * @description: ## 1
 * 快捷键:
 *  * 1. command+alt+click:快速查看方法具体实现
 *  * 2. command+alt+v: 快速创建变量，需要先将光标移动到变量表达，如new AccessTokenDTO()
 *  * 3. shift+enter: 快速换行并将光标移动到新行的最前边
 *  * 4. alt+enter: 快速修复异常
 *  * 5. Value注解后会到配置文件里读取其参数里的key的value，并将其赋值到变量中
 *  * 6. command+n: 快速创建get，set方法
 *  * 7. control+r: terminal中找mvn命令时，快速搜索命令
 * git命令
 *  * 1. 每次完成一部分Project，在终端中查看修改记录: git status；红色的返回结果为经过修改，目前还没放到暂存里
 *  * 2. 将所有修改文件丢到暂存里: git add .；"."表示当前目录
 *  * 3. 提交一个备注: git commit -m "备注内容"；
 *  * 4. 将其push到git仓库: git push
 *  * 5. 在README.md中添加备录，按着alt鼠标下拉，选中多行，同时添加，command+right同时移动光标到最右
 *
 * ## 1:
 * 执行run即可将整个Spring Boot项目运行起来；还是用Tomcat，但将其封装了起来
 * next-->Serving Web Content with Spring MVC https://spring.io/guides/gs/serving-web-content/ 在该网址找到相关
 * maven dependency，在pom.xml进行引入spring-boot-starter-thymeleaf
 * ## 2:
 * 根据Spring文档 https://spring.io/guides/gs/serving-web-content/ 创建web资源，写IndexController
 * ## 3:
 * a. 使用前端UI框架Bootstrap快速建立前端站点;下载Boostrap工具包，https://v3.bootcss.com/getting-started/，将解压出的文件夹
 *    放入resources/static目录；了解响应式布局，栅格系统，https://v3.bootcss.com/css/#grid
 * b. 参照https://v3.bootcss.com/getting-started/在index.html head标签中引入三个包，bootstrap.min.css，
 *    bootstrap-theme.min.css，和bootstrap.min.js，直接将static中的对应文件拖入即可
 * c. 在Bootstrap官网组件中找到导航条添加相关代码，复制粘贴到index body下
 * ## 4:
 * 实现Github登录
 * a. 在Github官网找到创建Github授权App的方法,Building OAuth Apps, https://developer.github.com/apps/building-oauth-apps/;
 * b. 按照步骤创建Github App
 * c. 在index.html中将登录按钮绑定url地址，调用Github authorize接口，GET https://github.com/login/oauth/authorize，并携带相关
 *    参数，client_id，redirect_uri，scope，state
 * e. User同意请求，GitHub回调，发送步骤c中预设的redirect_uri，同时携带一个code，接收code。创建AuthorizeController，获得code和
 *    state参数，准备进行下一步Github请求
 * f. 调用Github access_token接口，POST https://github.com/login/oauth/access_token，携带相同code；用POST，需要引入第三方工具
 *    OkHttp。查看OkHttp做POST请求的方法，https://square.github.io/okhttp/
 * g. 调用Github access_token接口需要传递5个参数，client_id，client_secret，code，redirect_uri，state；好的编程习惯，如果参
 *    数超过两个以上，最好不要放到方法的形参上，把它们封装成一个对象，AccessTokenDTO，放入一个包中，dto(data transfer object)
 * h. 继续f，创建GithubProvider类，参照 https://square.github.io/okhttp/ 做POST请求，用maven引入JAR包，创建getAccessToken()
 *    方法
 * i. POST请求成功获得access_token后，携带access_token，调用Github user api，GET https://api.github.com/user，获取Github用户
 *    信息，name，id，bio，把它们封装成一个对象，创建GithubUser，放入一个dto中
 * j. 在GithubProvider类中创建getUser()方法，用OkHttp做GET请求，将返回的Github用户信息封装到一个GithubUser对象中
 * ## 5
 * 配置application properties；目的是为了在不同的服务器环境，项目环境，能读取到不同的配置文件
 * ## 6
 * cookie和session，使登录成功后能保持一个登录态；session相当于你的银行账户，cookie相当于你的银行卡，去银行取钱需要提供银行卡才能使银
 * 行知道你的账户是什么，才能操作你的余额
 * a. 目标，没登录时页面右上角显示"我"，登录后显示名字
 * b. 修改AuthorizeController，拿到session
 * c. 回到index.html，从中拿到session，判断是否有session，有的话就展示名字，没有到话就登录，查看thymeleaf怎么查看session取值以及条件
 *    语句
 * ## 7
 * 重启服务器后，原来的登录态消失了，设法保留登录态，引入数据库概念；H2数据库 https://www.h2database.com/html/main.html；用maven仓
 * 库引入H2；
 * ## 8
 * 学习mybatis，集成Spring Boot和mybatis，http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/；
 * 引入依赖，复制官网代码到pom；mybatis作用：https://blog.csdn.net/qq_33509293/article/details/80280813；
 * mybatis setup需要连接池（DataSource)，通过连接池配置数据库（用户名，密码，地址...)29.1.2，Spring支持HikariCP连接池，
 * 引入该连接池依赖；将DataSource配置代码拷贝到application.properties；
 * https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support
 * 新建mapper包，UserMapper类
 * a. 任务：在AuthorizeController里获得user的时候存入数据库，创建token同时加入；创建model包，User类，dto是在网络之间传输的object，
 *    在数据库中用model；
 * ## 9
 * 登录成功后用java代码向前端写一个cookie；原理：以UUID token为依据来绑定前端和后端的登录状态，将生成的token值写入一个
 * token:token_value键值对，放入cookie中，当主页再次加载时，如果在数据库中发现该token_value，就确认已经登录成功
 * a. 需要在indexController里写一个数据库token获取方法，注入一个UserMapper，因为这里边才可以访问数据库的user
 * ## 10
 * 学习使用Flyway，https://flywaydb.org/
 * ## 11
 * 创建publish.html和PublishController
 * ## 12
 * 完成文章发布功能
 * a. 发布文章提交到服务器端需要存在一个数据库；terminal中找mvn命令时，control+r快速搜索命令；用mvn命令创建数据库question
 * b. 创建QuestionMapper，Question；重新构建PublishController，如果是post就执行请求，如果是get就渲染页面
 * ## 13
 * 完成首页列表
 * a. 给user添加图像参数；在migration里添加数据库属性
 * b. 学习使用Lombok；@Data注解，不再需要构建set，get方法
 *
 */
@SpringBootApplication
public class ThirdquadranApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdquadranApplication.class, args);
    }


}
