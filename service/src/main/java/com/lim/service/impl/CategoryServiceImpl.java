package com.lim.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lim.service.CategoryService;
import com.lim.dto.CategoryDTO;
import com.lim.dto.CategoryPageQueryDTO;
import com.lim.entity.Category;
import com.lim.exception.DeletionNotAllowedException;
import com.lim.mapper.CategoryMapper;
import com.lim.mapper.DishMapper;
import com.lim.mapper.SetMealMapper;
import com.lim.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

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
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        categoryMapper.updateCategory(category);
    }

    @Override
    public void updateCategoryStatus(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();

        categoryMapper.updateCategory(category);
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setStatus(0);
        BeanUtils.copyProperties(categoryDTO,category);

        categoryMapper.insertCategory(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if(!dishMapper.selectDishByCategoryId(id).isEmpty()){
            throw new DeletionNotAllowedException("该分类还有菜品使用，暂时不能删除~~");
        }
        if (setMealMapper.selectSetMealByCategoryId(id) != null){
            throw new DeletionNotAllowedException("该分类还有套餐使用，暂时不能删除~~");
        }
        categoryMapper.deleteCategoryById(id);
    }

    @Override
    public List<Category> selectCategory(Integer type) {
        return categoryMapper.selectCategoryByType(type);
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }

}
