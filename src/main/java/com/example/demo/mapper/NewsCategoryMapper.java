package com.example.demo.mapper;

import com.example.demo.model.NewsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by WY on 2017/8/27.
 */
@Mapper
public interface NewsCategoryMapper {
    @Select("select * from newscategory")
    List<NewsCategory> getNewsCategory();
}
