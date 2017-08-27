package com.example.demo.controller;

import com.example.demo.model.NewsCategory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by WY on 2017/8/27.
 */
@RestController
public class AController {
    private final SqlSession sqlSession;

    public AController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @RequestMapping("/GET/newsCategory")
    @Cacheable("newsCategory")
    public List<NewsCategory> getNewsCategory() {
        System.out.print("cache is coming");
        return sqlSession.selectList("getNewsCategory");
    }
}
