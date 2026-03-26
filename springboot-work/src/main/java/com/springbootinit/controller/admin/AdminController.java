package com.springbootinit.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.model.dto.LoginRequest;
import com.springbootinit.model.vo.LoginVO;
import com.springbootinit.service.UrUsersService;
import com.springbootinit.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员接口
 */
@Api(tags = "管理员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UrUsersService urUsersService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 管理员登录
     *
     * @param loginRequest 登录请求参数（用户名、密码）
     * @return 登录成功返回用户信息，JWT令牌在响应头的Cookie中
     */
    @ApiOperation(value = "管理员登录", notes = "管理员使用用户名和密码登录，成功后JWT令牌会在响应头的Cookie中返回")
    @PostMapping("/login")
    public BaseResponse<LoginVO> login(@RequestBody LoginRequest loginRequest, javax.servlet.http.HttpServletResponse response) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        queryWrapper.eq("user_type", 3);
        UrUsers user = urUsersService.getOne(queryWrapper);

        if (user == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }

        if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        if (user.getStatus() != 1) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUserType());

        // 设置JWT令牌到Cookie（直接设置响应头以支持SameSite属性）
        String cookieValue = String.format("Authorization=%s; HttpOnly; Secure=false; SameSite=Strict; Path=/; Max-Age=%d",
                token, 24 * 60 * 60);
        response.addHeader("Set-Cookie", cookieValue);

        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setUserType(user.getUserType());
        loginVO.setAvatarUrl(user.getAvatarUrl());
        loginVO.setUserRote(user.getUserRote());

        return ResultUtils.success(loginVO);
    }

    /**
     * 管理员注册
     *
     * @param registerRequest 注册请求参数（用户名、密码）
     * @return 注册成功返回用户ID
     */
    @ApiOperation(value = "管理员注册", notes = "注册新的管理员账号，user_type自动设置为3")
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody LoginRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerRequest.getUsername());
        long count = urUsersService.count(queryWrapper);
        if (count > 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        UrUsers user = new UrUsers();
        user.setUsername(registerRequest.getUsername());
        user.setPasswordHash(registerRequest.getPassword());
        user.setUserType(3);
        user.setStatus(1);

        boolean save = urUsersService.save(user);
        if (!save) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "注册失败");
        }

        return ResultUtils.success(user.getId());
    }

}

