package com.lim.controller.admin;

import com.lim.Service.CategoryService;
import com.lim.dto.CategoryDTO;
import com.lim.dto.CategoryPageQueryDTO;
import com.lim.entity.Category;
import com.lim.result.PageResult;
import com.lim.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result pagingQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult<Category> queryResult= categoryService.pagingQuery(categoryPageQueryDTO);
        return Result.success(queryResult);
    }

    @PutMapping()
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
        return Result.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public Result updateCategoryStatus(@PathVariable Integer status,Long id){
        categoryService.updateCategoryStatus(status,id);
        return Result.success();
    }

    @PostMapping()
    public Result saveCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.saveCategory(categoryDTO);
        return Result.success("添加成功");
    }

    @DeleteMapping()
    public Result delete(Long id){
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result selectEmp(Integer type){
        List<Category> categories = categoryService.selectCategory(type);
        return Result.success(categories);
    }
}
