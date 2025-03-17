package com.lim;

import com.lim.dto.EmployeeLoginDTO;
import com.lim.entity.Employee;

public interface EmpService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
