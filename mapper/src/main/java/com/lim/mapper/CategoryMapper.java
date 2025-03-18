package com.lim.mapper;

import com.lim.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> pagingQuery(Category category);

    void updateCategory(Category category);

    void insertCategory(Category category);
}
