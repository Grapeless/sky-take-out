package com.lim.controller;

import com.lim.Service.EmpService;
import com.lim.constant.JwtClaimsConstant;
import com.lim.dto.EmployeeDTO;
import com.lim.dto.EmployeeLoginDTO;
import com.lim.dto.EmployeePageQueryDTO;
import com.lim.entity.Employee;
import com.lim.properties.JwtProperties;
import com.lim.result.PageResult;
import com.lim.result.Result;
import com.lim.utils.JwtUtil;
import com.lim.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmpController {
    @Autowired
    private EmpService empService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        Employee employee = empService.login(employeeLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    @PostMapping()
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        empService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("page")
    public Result pagingQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult<Employee> queryResult = empService.pagingQuery(employeePageQueryDTO);
        return Result.success(queryResult);
    }

    @PostMapping("/status/{status}")
    public Result updateEmpStatus(@PathVariable Integer status,Long id){
        empService.updateEmpStatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result selectEmp(@PathVariable Long id){
        Employee e = empService.selectEmp(id);
        return Result.success(e);
    }

    @PutMapping()
    public Result updateEmp(@RequestBody EmployeeDTO employeeDTO){
        empService.updateEmp(employeeDTO);
        return Result.success("修改成功");
    }
}
