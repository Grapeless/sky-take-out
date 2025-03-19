package com.lim.Service.impl;

import com.github.pagehelper.Page;
import com.lim.Service.EmpService;
import com.lim.constant.MessageConstant;
import com.lim.constant.PasswordConstant;
import com.lim.constant.StatusConstant;
import com.lim.context.BaseContext;
import com.lim.dto.EmployeeDTO;
import com.lim.dto.EmployeeLoginDTO;
import com.lim.dto.EmployeePageQueryDTO;
import com.lim.entity.Employee;
import com.lim.exception.AccountLockedException;
import com.lim.exception.AccountNotFoundException;
import com.lim.exception.PasswordErrorException;
import com.lim.mapper.EmployeeMapper;
import com.lim.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) throws AccountNotFoundException {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        //在ThreadLocal中取出登录用户的id
        Long userId = BaseContext.getCurrentId();
        Employee employee = Employee.builder()
                .password(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()))
                .status(StatusConstant.ENABLE)
                .build();

        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.save(employee);
    }

    @Override
    public PageResult<Employee> pagingQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> queryResult = (Page<Employee>) employeeMapper.pagingQuery(employeePageQueryDTO.getName());

        return new PageResult<>(queryResult.getTotal(),queryResult.getResult());

    }

    @Override
    public void updateEmpStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
         employeeMapper.updateEmp(employee);
    }

    @Override
    public Employee selectEmp(Long id) {
        return employeeMapper.selectEmpById(id);
    }

    @Override
    public void updateEmp(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employeeMapper.updateEmp(employee);
    }
}
