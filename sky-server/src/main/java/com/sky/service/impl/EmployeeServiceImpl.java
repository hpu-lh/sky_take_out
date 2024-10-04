package com.sky.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sun.management.snmp.jvmmib.EnumJvmMemPoolCollectThreshdSupport;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码对比
        password=DigestUtils.md5DigestAsHex(password.getBytes());
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
    public Integer insertEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性拷贝，（用于连个对象的属性名一致）
        BeanUtils.copyProperties(employeeDTO,employee);
        //默认员工账号状态正常
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //TODO 后期需要改为当前登录用户的id
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        Integer result=employeeMapper.insertEmployee(employee);
        return result;
    }

    @Override
    public PageResult queryEmployeeForPage(EmployeePageQueryDTO employeePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.queryEmployeeForPage(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        PageResult pageResult = new PageResult(total, records);
        return pageResult;
    }

    @Override
    public Integer updateEmployeeStatus(Integer status, Long id) {
        Integer result=employeeMapper.updateEmployeeStatus(status,id);
        return result;
    }

    @Override
    public Employee queryEmployeeById(Long id) {
        Employee employee=employeeMapper.selectEmployeeById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public Result updateEmployee(EmployeeDTO employeeDTO) {
        Employee em = employeeMapper.getByUsername(employeeDTO.getUsername());
        if(em!=null){
            if(em.getId()!=employeeDTO.getId()){
                return Result.error("用户名已存在");
            }else{
                Employee employee = new Employee();
                BeanUtils.copyProperties(employeeDTO,employee);
                Integer i=employeeMapper.updateEmployee(employee);
                if(i>0){
                    if(i==1){
                        return Result.success();
                    }else{
                        return Result.error("修改员工信息错误");
                    }
                }else{
                    return Result.error("修改员工信息错误");
                }
            }
        }else{
            Employee employee = new Employee();
            BeanUtils.copyProperties(employeeDTO,employee);
//            employee.setUpdateTime(LocalDateTime.now());
//            employee.setUpdateUser(BaseContext.getCurrentId());
            Integer i=employeeMapper.updateEmployee(employee);
            if(i>0){
                if(i==1){
                    return Result.success();
                }else{
                    return Result.error("修改员工信息错误");
                }
            }else{
                return Result.error("修改员工信息错误");
            }
        }
    }
}
