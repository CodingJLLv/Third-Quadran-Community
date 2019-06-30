package thirdquadrancommunity.thirdquadran.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import thirdquadrancommunity.thirdquadran.model.User;

/**
 * @author: Jinglei
 * @date: 2019-06-29
 * @description:
 */
@Mapper
public interface UserMapper {
    // 使用与select方法同一目录的insert方法，将user的各个属性都替换掉
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token}," +
            "#{gmtCreate},#{gmtModified})")
    void insert(User user);
    //    #{}时mybatis编译的时候，当形参是个类时把方法形参放到大括号里去，如上边的user，不是类时，形参前加Param注解，
//    @Param("token") String token可理解为给String token形参命名为"token"
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
