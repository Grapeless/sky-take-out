package com.lim.mapper;

import com.lim.anno.AutoFill;
import com.lim.entity.Employee;
import com.lim.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(type = OperationType.INSERT)
    @Insert("insert into employee(name, username, password, phone, sex, id_number,status,create_time, update_time, create_user, update_user) VALUES " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Employee employee);

    List<Employee> pagingQuery(@Param("name") String name);

    @AutoFill(type = OperationType.UPDATE)
    void updateEmp(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee selectEmpById(Long id);
}
