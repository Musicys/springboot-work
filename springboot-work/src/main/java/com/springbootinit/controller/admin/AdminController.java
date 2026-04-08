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
        // 设置JWT令牌到Cookie
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("Authorization", token);
        cookie.setPath("/");             // 设置路径，确保全站可访问
        cookie.setMaxAge(24 * 60 * 60);   // 设置有效期（秒）
        cookie.setHttpOnly(true);          // 防止 XSS 攻击
        cookie.setSecure(false);           // 仅 HTTPS 传输（生产环境建议开启）
        response.addCookie(cookie);
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setUserType(user.getUserType());
        loginVO.setAvatarUrl(user.getAvatarUrl());
        loginVO.setUserRote(user.getUserRote());
        loginVO.setToken(token);

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

    /**
     * 获取登录状态
     *
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 登录状态信息
     */
    @ApiOperation(value = "获取登录状态", notes = "获取当前管理员的登录状态")
    @PostMapping("/getLoginStatus")
    public BaseResponse<LoginVO> getLoginStatus(javax.servlet.http.HttpServletRequest request) {
        // 从Cookie中获取token
        String token = null;
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }

        // 验证token
        try {
            Long userId = jwtUtils.getUserIdFromToken(token);
            Integer userType = jwtUtils.getUserTypeFromToken(token);

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userId);
            queryWrapper.eq("user_type", 3);
            UrUsers user = urUsersService.getOne(queryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() != 1) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            }

            // 返回用户信息
            LoginVO loginVO = new LoginVO();
            loginVO.setUserId(user.getId());
            loginVO.setUsername(user.getUsername());
            loginVO.setUserType(user.getUserType());
            loginVO.setAvatarUrl(user.getAvatarUrl());
            loginVO.setUserRote(user.getUserRote());

            return ResultUtils.success(loginVO);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 注销登录
     *
     * @param response 响应对象，用于清除Cookie
     * @return 注销结果
     */
    @ApiOperation(value = "注销登录", notes = "清除登录状态，移除认证Cookie")
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(javax.servlet.http.HttpServletResponse response) {
        // 清除Authorization Cookie
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("Authorization", "");
        cookie.setPath("/");             // 设置路径，确保全站可访问
        cookie.setMaxAge(0);               // 设置有效期为0，表示删除cookie
        cookie.setHttpOnly(true);          // 防止 XSS 攻击
        cookie.setSecure(false);           // 仅 HTTPS 传输（生产环境建议开启）
        response.addCookie(cookie);

        return ResultUtils.success(true);
    }
}

