package com.lim.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lim.Service.CategoryService;
import com.lim.context.BaseContext;
import com.lim.dto.CategoryDTO;
import com.lim.dto.CategoryPageQueryDTO;
import com.lim.entity.Category;
import com.lim.mapper.CategoryMapper;
import com.lim.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResult<Category> pagingQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Category category = Category.builder()
                .name(categoryPageQueryDTO.getName())
                .type(categoryPageQueryDTO.getType())
                .build();
        Page<Category> categories = (Page<Category>) categoryMapper.pagingQuery(category);
        return new PageResult<>(categories.getTotal(), categories.getResult());
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();

        BeanUtils.copyProperties(categoryDTO, category);

        categoryMapper.updateCategory(category);
    }

    @Override
    public void updateCategoryStatus(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .updateUser(BaseContext.getCurrentId())
                .updateTime(LocalDateTime.now())
                .build();

        categoryMapper.updateCategory(category);
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .status(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createUser(BaseContext.getCurrentId())
                .updateUser(BaseContext.getCurrentId())
                .build();

        BeanUtils.copyProperties(categoryDTO,category);

        categoryMapper.insertCategory(category);
    }
}
