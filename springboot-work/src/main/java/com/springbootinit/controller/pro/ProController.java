package com.springbootinit.controller.pro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.UrMerchantProfiles;
import com.springbootinit.model.domain.UrUsers;
import com.springbootinit.model.dto.LoginRequest;
import com.springbootinit.model.dto.ProRegisterRequest;
import com.springbootinit.model.vo.LoginVO;
import com.springbootinit.service.UrMerchantProfilesService;
import com.springbootinit.service.UrUsersService;
import com.springbootinit.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商家接口
 */
@Api(tags = "商家接口")
@RestController
@RequestMapping("/pro")
public class ProController {

    @Autowired
    private UrUsersService urUsersService;

    @Autowired
    private UrMerchantProfilesService urMerchantProfilesService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 商家登录
     *
     * @param loginRequest 登录请求参数（用户名、密码）
     * @return 登录成功返回用户信息，JWT令牌在响应头的Cookie中
     */
    @ApiOperation(value = "商家登录", notes = "商家使用用户名和密码登录，成功后JWT令牌会在响应头的Cookie中返回")
    @PostMapping("/login")
    public BaseResponse<LoginVO> login(@RequestBody LoginRequest loginRequest, javax.servlet.http.HttpServletResponse response) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<UrUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        queryWrapper.eq("user_type", 2);
        UrUsers user = urUsersService.getOne(queryWrapper);

        if (user == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "商家不存在");
        }

        if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        if (user.getStatus() == 0) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
        } else if (user.getStatus() == 2) {
            return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR,
                    "账号正在审核中，暂时无法登录");
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
     * 商家注册
     *
     * @param registerRequest 注册请求参数（用户名、密码、企业名称、联系电话）
     * @return 注册成功返回用户ID
     */
    @ApiOperation(value = "商家注册", notes = "注册新的商家账号，同时创建商家档案信息")
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody ProRegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null
                || registerRequest.getCompanyName() == null) {
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
        user.setUserType(2);
        user.setStatus(2); // 1:审核中

        boolean save = urUsersService.save(user);
        if (!save) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "注册失败");
        }

        // 检查商家档案是否已存在
        UrMerchantProfiles existingProfile = urMerchantProfilesService.getById(user.getId());
        UrMerchantProfiles profile;

        if (existingProfile != null) {
            // 如果档案已存在，更新现有记录
            profile = existingProfile;
            profile.setCompanyName(registerRequest.getCompanyName());
            profile.setContactPhone(registerRequest.getContactPhone());
            profile.setLocation(registerRequest.getLocation());
            profile.setLegalPerson(registerRequest.getLegalPerson());
            profile.setLegalIdCard(registerRequest.getLegalIdCard());
            profile.setCompanyImages(registerRequest.getCompanyImages());
            if (registerRequest.getRegisteredCapital() != null) {
                profile.setRegisteredCapital(new java.math.BigDecimal(registerRequest.getRegisteredCapital()));
            }
            profile.setCompanyAddress(registerRequest.getCompanyAddress());
            profile.setCompanyIntro(registerRequest.getCompanyIntro());
            profile.setIsSubAccount(0);
            urMerchantProfilesService.updateById(profile);
        } else {
            // 如果档案不存在，创建新记录
            profile = new UrMerchantProfiles();
            profile.setUserId(user.getId());
            profile.setCompanyName(registerRequest.getCompanyName());
            profile.setContactPhone(registerRequest.getContactPhone());
            profile.setLocation(registerRequest.getLocation());
            profile.setLegalPerson(registerRequest.getLegalPerson());
            profile.setLegalIdCard(registerRequest.getLegalIdCard());
            profile.setCompanyImages(registerRequest.getCompanyImages());
            if (registerRequest.getRegisteredCapital() != null) {
                profile.setRegisteredCapital(new java.math.BigDecimal(registerRequest.getRegisteredCapital()));
            }
            profile.setCompanyAddress(registerRequest.getCompanyAddress());
            profile.setCompanyIntro(registerRequest.getCompanyIntro());
            profile.setIsSubAccount(0);
            urMerchantProfilesService.save(profile);
        }

        return ResultUtils.success(user.getId());
    }

    /**
     * 获取登录状态
     *
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 登录状态信息
     */
    @ApiOperation(value = "获取登录状态", notes = "获取当前商家的登录状态")
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
            queryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(queryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法登录");
            }

            // 返回用户信息
            LoginVO loginVO = new LoginVO();
            loginVO.setUserId(user.getId());
            loginVO.setUsername(user.getUsername());
            loginVO.setUserType(user.getUserType());
            loginVO.setAvatarUrl(user.getAvatarUrl());
            loginVO.setUserRote(user.getUserRote());
            loginVO.setToken(jwtUtils.generateToken(user.getId(), user.getUserType()));
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

    /**
     * 获取商家档案信息
     *
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @return 商家档案信息
     */
    @ApiOperation(value = "获取商家档案信息", notes = "根据当前登录商家的token获取档案信息")
    @PostMapping("/getProfile")
    public BaseResponse<UrMerchantProfiles> getProfile(javax.servlet.http.HttpServletRequest request) {
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

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", userId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 获取商家档案信息
            UrMerchantProfiles profile = urMerchantProfilesService.getById(userId);
            if (profile == null) {
                return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "商家档案不存在");
            }

            return ResultUtils.success(profile);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }

    /**
     * 修改商家档案信息
     *
     * @param request 请求对象，用于获取Cookie中的JWT令牌
     * @param profile 商家档案信息
     * @return 修改结果
     */
    @ApiOperation(value = "修改商家档案信息", notes = "根据当前登录商家的token修改档案信息")
    @PostMapping("/updateProfile")
    public BaseResponse<Boolean> updateProfile(javax.servlet.http.HttpServletRequest request, @RequestBody UrMerchantProfiles profile) {
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

            // 验证用户是否存在且类型正确
            QueryWrapper<UrUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", userId);
            userQueryWrapper.eq("user_type", 2);
            UrUsers user = urUsersService.getOne(userQueryWrapper);

            if (user == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
            }

            if (user.getStatus() == 0) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
            } else if (user.getStatus() == 2) {
                return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "账号正在审核中，暂时无法操作");
            }

            // 确保修改的是当前用户的档案
            profile.setUserId(userId);

            // 更新商家档案信息
            boolean updated = urMerchantProfilesService.updateById(profile);
            if (!updated) {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "修改失败");
            }

            return ResultUtils.success(true);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "登录已过期");
        }
    }
}
