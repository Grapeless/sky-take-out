package com.lim.mapper;

import com.lim.anno.AutoFill;
import com.lim.entity.Category;
import com.lim.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> pagingQuery(Category category);

    @AutoFill(type = OperationType.UPDATE)
    void updateCategory(Category category);

    @AutoFill(type = OperationType.INSERT)
    void insertCategory(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteCategoryById(Long id);

    @Select("select * from category where type = #{type}")
    List<Category> selectCategoryByType(Integer type);

    List<Category> list(@Param("type") Integer type);
}
