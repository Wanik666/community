package wang.kingweb.community.mapper;

import org.apache.ibatis.annotations.*;
import wang.kingweb.community.entity.User;

@Mapper
public interface UserMapper {

    @Select("select * from user where account_id = #{accountId}")
    User selectUserByAccountId(@Param("accountId") Long accountId);

    @Insert("insert into user (account_id,name,token,create_time,modified_time,avatar_url) values (#{accountId},#{" +
            "name},#{token},#{createTime},#{modifiedTime},#{avatarUrl})")
    void addUser(User user);

    @Update("update user set token = #{token},name = #{name},modified_time = #{modifiedTime},avatar_url=#{avatarUrl} where id = #{id}")
    void updateUser(User user);

    @Select("select * from user where token = #{token}")
    User selectUserByToken(@Param("token") String token);
}
