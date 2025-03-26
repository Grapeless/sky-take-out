package com.lim.service;

import com.lim.dto.EmployeeDTO;
import com.lim.dto.EmployeeLoginDTO;
import com.lim.dto.EmployeePageQueryDTO;
import com.lim.entity.Employee;
import com.lim.result.PageResult;

public interface EmpService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult<Employee> pagingQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void updateEmpStatus(Integer status, Long id);

    Employee selectEmp(Long id);

    void updateEmp(EmployeeDTO employeeDTO);
}
