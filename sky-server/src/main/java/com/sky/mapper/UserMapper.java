package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.annotation.MatchesPattern;
import java.util.Map;

@Mapper
public interface UserMapper {
    User getUserByOpenId(String openId);

    void insertUser(User user);

    @Select("select * from user where id=#{id}")
    User getById(Long userId);

    Integer selectCountOfUser(Map map);
}
