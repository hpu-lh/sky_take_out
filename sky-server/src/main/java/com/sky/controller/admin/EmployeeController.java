package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags="员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        Integer result=employeeService.insertEmployee(employeeDTO);
        if(result>0){
            if(result>1){
                return Result.error("新增员工失败");
            }else{
                return Result.success();
            }
        }else{
            return Result.error("新增员工失败");
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询员工列表")
    public Result queryEmployeeForPage(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询参数为：{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.queryEmployeeForPage(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改员工账号状态")
    public Result updateEmployeeStatus(@PathVariable Integer status,Long id){
        log.info("修改员工账号状态:{},{}",status,id);
        Integer result=employeeService.updateEmployeeStatus(status,id);
        if(result>0){
            if(result==1){
                return Result.success();
            }else{
                return Result.error("修改员工账号状态错误");
            }
        }else{
            return Result.error("修改员工账号状态错误");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询employee")
    public Result queryEmployeeById(@PathVariable Long id){
        Employee employee=employeeService.queryEmployeeById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息:{}",employeeDTO);
        Result result=employeeService.updateEmployee(employeeDTO);
        return result;
    }

}
