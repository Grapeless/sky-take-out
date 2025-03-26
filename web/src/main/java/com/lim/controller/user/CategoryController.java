package com.lim.controller.user;

import com.lim.service.CategoryService;
import com.lim.entity.Category;
import com.lim.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
