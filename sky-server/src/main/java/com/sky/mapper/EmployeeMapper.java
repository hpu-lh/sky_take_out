package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    @AutoFill(value = OperationType.INSERT)
    Integer insertEmployee(Employee employee);

    Page<Employee> queryEmployeeForPage(EmployeePageQueryDTO employeePageQueryDTO);

    Integer updateEmployeeStatus(@Param("status") Integer status, @Param("id") Long id);

    Employee selectEmployeeById(@Param("id") Long id);

    @AutoFill(value = OperationType.UPDATE)
    Integer updateEmployee(Employee employee);
}
