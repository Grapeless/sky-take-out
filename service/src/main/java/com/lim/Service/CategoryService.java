package com.lim.Service;

import com.lim.dto.CategoryDTO;
import com.lim.dto.CategoryPageQueryDTO;
import com.lim.entity.Category;
import com.lim.result.PageResult;

public interface CategoryService {
    PageResult<Category> pagingQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void updateCategory(CategoryDTO categoryDTO);

    void updateCategoryStatus(Integer status, Long id);

    void saveCategory(CategoryDTO categoryDTO);
}
