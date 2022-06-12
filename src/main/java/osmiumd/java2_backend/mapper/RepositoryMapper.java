package osmiumd.java2_backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import osmiumd.java2_backend.dto.Language;
import osmiumd.java2_backend.dto.LanguageSum;
import osmiumd.java2_backend.dto.Repository;
import osmiumd.java2_backend.dto.Topic;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Mapper
public interface RepositoryMapper {

    @Select("select * from repository")
    List<Repository> allRepos();

    @Select("select * from repository where id=#{id}")
    List<Repository> searchRepos(int id);

    @Insert("insert into repository(id, name, stars, forks, main_language, create_time) " +
            "VALUES (#{id},#{name},#{stars},#{forks},#{main_language},#{create_time})")
    void insertRepos(Repository repository);

    @Insert("insert into repository_language(repos_id, name, count) " +
            "VALUES (#{repos_id}, #{name}, #{count})")
    void insertLang(Language language);

    @Insert("insert into repository_topic(repos_id, name) " +
            "VALUES (#{repos_id}, #{name})")
    void insertTopic(Topic topic);

    @Select("select count(*) from repository where main_language = #{language}")
    Integer languageCount(String language);

    @Select("select count(*) from repository")
    Integer allCount();

    List<LanguageSum> languageSum(String language, Integer aggr, Integer limit);

    List<LanguageSum> languageTopic(String language, Integer aggr, Integer limit);

    List<LanguageSum> reposCreate(Date qStart, Date qEnd, Integer limit);

    @Select("select * from repository order by stars desc limit #{limit} offset #{offset}")
    List<Repository> getPage(Integer limit, Integer offset);

    Integer searchPageCount(Integer limit, Integer offset, Date start, Date end, String language, String search);

    List<Repository> searchPage(Integer limit, Integer offset, Date start, Date end, String language, String search);
}
