package thirdquadrancommunity.thirdquadran.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import thirdquadrancommunity.thirdquadran.model.Question;

/**
 * @author: Jinglei
 * @date: 2019-06-29
 * @description:
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, " +
            "tag) values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    public void create(Question question);
}
