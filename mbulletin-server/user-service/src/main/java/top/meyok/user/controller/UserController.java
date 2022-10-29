package top.meyok.user.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.meyok.user.constants.AccountStatusEnum;
import top.meyok.user.pojo.dto.*;
import top.meyok.user.pojo.vo.BaseVO;
import top.meyok.user.pojo.dto.UserCertifyInfoDTO;
import top.meyok.user.pojo.vo.UserLoginInfoVO;
import top.meyok.user.service.CheckService;
import top.meyok.user.service.MailService;
import top.meyok.user.service.UserService;
import top.meyok.user.util.RandomCharacterUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/8/29 3:17
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    @Resource
    private CheckService checkService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // 图片校验码相关接口

    /**
     * 获得校验码图片
     * @param request 用于获取session来存储校验码
     * @param response 用于写回校验码图片
     */
    @RequestMapping(value = "/checkPicture", method = RequestMethod.GET)
    public void getCheckPicture(HttpServletRequest request, HttpServletResponse response) {
        // 生成随机校验码
        String pictureCheckCode = RandomCharacterUtils.getPictureCheckCode(4);
        // 存储校验码
        HttpSession session = request.getSession();

        LOGGER.info("try to make check code pic: SESSIONID={}, picCheckCode={}", session.getId(), pictureCheckCode);

        session.setAttribute("pictureCheckCode", pictureCheckCode);
        // 画图
        try {
            checkService.fillInCheckCodePicture(100, 50, pictureCheckCode, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("check code pic return file: error_info={}", e.getMessage());
        }

    }


    /**
     * 检查图片校验码是否正确
     * @param checkCode 为用户填入校验码。
     * @param session 用于获取正确的校验码
     * @return BaseVO中object成员变量为CheckVO
     */
    @RequestMapping(value = "/pictureCodeCheck/{checkCode}", method = RequestMethod.GET)
    @ResponseBody
    public BaseVO pictureChecked(@PathVariable(name = "checkCode") String checkCode, HttpSession session) {

        LOGGER.info("try to certify pic check code: SESSIONID={}, fillCheckCode={}", session.getId(), checkCode);

        String correctCheckCode = (String) session.getAttribute("pictureCheckCode");
        if (!ObjectUtils.isEmpty(correctCheckCode)) {
            if (correctCheckCode.equals(checkCode)) {
                LOGGER.info("pic check code is correct: SESSIONID={}", session.getId());
                return new BaseVO(200, true, "correct");
            } else {
                LOGGER.info("pic check code is incorrect: SESSIONID={}", session.getId());
                return new BaseVO(200, false, "incorrect");
            }
        } else {
            LOGGER.warn("con not get pic code check from session {}", session.getId());
            return new BaseVO(200, false, "校验码未构造！");
        }
    }


    // 用户注册相关接口

    /**
     * 检查用户邮箱状态
     * @param email 邮箱地址
     * @return BaseVO中object成员变量,0为未注册、1为注册未激活、2为注册已激活、3为账户冻结、4为服务错误。
     */
    @RequestMapping(value = "/mailRegistered/{email}", method = RequestMethod.GET)
    @ResponseBody
    public BaseVO mailRegistered(@PathVariable(value = "email") String email) {

        LOGGER.info("try to query email status: email={}", email);

        AccountStatusEnum mailStatus = userService.getMailStatus(email);

        if (ObjectUtils.isEmpty(mailStatus)) {
            LOGGER.error("userService.getMailStatus can not call");
            return new BaseVO(500, 5, "服务器出错！");
        } else {
            return new BaseVO(200, mailStatus.ordinal(), "成功查询状态。0为未注册、1为注册未激活、2为注册已激活、3为账户冻结、4为服务错误。");
        }

    }



    /**
     * 注册用户信息
     * @param userRegisterInfoDTO 用户注册信息，包含username、email、password。
     */
    @RequestMapping(value = "/registerUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseVO registerUserInfo(@RequestBody UserRegisterInfoDTO userRegisterInfoDTO) {
        LOGGER.info("try to register user info");
        Boolean registered = userService.registerUserInfo(userRegisterInfoDTO);

        if (ObjectUtils.isEmpty(registered)) {
            LOGGER.error("userService.registerUserInfo can not call");
            return new BaseVO(500, false, "服务器出错！");
        } else {
            if (registered) {
                LOGGER.info("register user info success");

                // TODO: MeYok 2022/10/25 发送认证邮件
                String email = userRegisterInfoDTO.getEmail();
                UserCertifyInfoDTO userCertifyInfo = userService.getUserCertifyInfoDTO(email);
                MailDTO mail = new MailDTO();
                String[] tos = {email};
                mail.setTos(tos);
                mail.setSubject("欢迎注册MBulletin，请完成邮箱认证！");
                String content = "点击以下链接，即可完成邮箱认证。</br><a href=\"http://localhost:3000/activate/" + email + "/" + userCertifyInfo.getCheckCode() + "\">MBulletin账户激活</a>";
                mail.setContent(content);
                mailService.sendHtmlMail(mail);


                return new BaseVO(200, true, "用户信息注册成功！");
            } else {
                LOGGER.warn("register user info failed: registerUserInfo={}", userRegisterInfoDTO);
                return new BaseVO(200, false, "用户信息注册失败！");
            }
        }

    }


    /**
     * 邮箱校验码激活
     * @param emailCheckCodeDTO
     * @return 0为服务器错误，1为账户已激活过无需再次激活，2为校验码错误激活失败，3为激活成功。
     */
    @RequestMapping(value = "/activateAccount", method = RequestMethod.POST)
    @ResponseBody
    public BaseVO activateAccount(@RequestBody EmailCheckCodeDTO emailCheckCodeDTO) {

        String email = emailCheckCodeDTO.getEmail();
        String checkCode = emailCheckCodeDTO.getCheckCode();

        LOGGER.info("try to activate account: email={}", email);

        UserCertifyInfoDTO userCertifyInfo = userService.getUserCertifyInfoDTO(email);

        if (ObjectUtils.isEmpty(userCertifyInfo)) {
            // 不存在该用户
            return new BaseVO(200, 4, "用户不存在！");

        } else {
            Byte certified = userCertifyInfo.getCertified();
            String correct_checkCode = userCertifyInfo.getCheckCode();

            if (ObjectUtils.isEmpty(certified) || ObjectUtils.isEmpty(correct_checkCode)) {
                // 获取信息错误
                LOGGER.error("user certify info store error: errorUserCertifyInfo={}", userCertifyInfo);
                return new BaseVO(500, 0, "服务器错误！");
            } else {
                if (certified == 1) {
                    // 已经激活过
                    return new BaseVO(200, 1, "用户已激活过，无需再次激活！");
                } else {
                    // 尚未激活
                    if (correct_checkCode.equals(checkCode)) {
                        // 校验成功，立即激活
                        userService.activateUserAccountInfo(email);
                        return new BaseVO(200, 3, "成功激活！");
                    } else {
                        // 校验失败
                        return new BaseVO(200, 2, "校验码错误！");
                    }
                }
            }
        }
    }


    // 用户登录登出接口

    /**
     * 用户登录接口
     * @param emailPasswordDTO 包含用户登录所使用的email、password
     * @return UserLoginInfoVO对象。
     * accountStatus为账户状态，0、1、2、3、4分别表示未注册、未激活、可用、冻结、服务错误；
     * email为此次登录邮箱；
     * username为用户名，只有登录成功才可以获取；
     * loggedIn为是否登录成功，却决于shiro凭证比对是否成功，0、1分别表示失败、成功。
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseVO login(@RequestBody EmailPasswordDTO emailPasswordDTO, HttpSession session) {
        String email = emailPasswordDTO.getEmail();

        LOGGER.info("try to login in: email={}, SESSIONID={}", email, session.getId());


        UserCertifyInfoDTO userCertifyInfo = userService.getUserCertifyInfoDTO(email);
        Byte certified = userCertifyInfo.getCertified();

        UserLoginInfoVO userLoginInfo = new UserLoginInfoVO();
        userLoginInfo.setEmail(email);
        userLoginInfo.setLoggedIn(0);

        // 判断用户是否注册、激活
        if (ObjectUtils.isEmpty(certified)) {
            // 账户未注册
            userLoginInfo.setAccountStatus(0);
            LOGGER.info("log in fail, unknown account: email={}", email);
            return new BaseVO(200, userLoginInfo, "账户未注册！");
        } else if (certified == 0) {
            // 账户未激活
            userLoginInfo.setAccountStatus(1);
            LOGGER.info("account does not activate: email={}", email);
            return new BaseVO(200, userLoginInfo, "账户未激活！");
        }

        // 查询用户基本信息，为登录成功返回账户信息
        UserBaseInfoDTO userBaseInfo = userService.getUserBaseInfo(email);
        if (ObjectUtils.isEmpty(userBaseInfo)) {
            // 未找到相应信息，但之前未判断成功，服务器出现了错误！
            userLoginInfo.setAccountStatus(4);
            LOGGER.error("log in fail, server error");
            return new BaseVO(500, userLoginInfo, "服务器错误！");
        }

        Subject subject = SecurityUtils.getSubject();
        try {

            subject.login(new UsernamePasswordToken(emailPasswordDTO.getEmail(), emailPasswordDTO.getPassword()));

            // 登录成功
            LOGGER.info("log in success: email={}, SESSIONID={}", emailPasswordDTO.getEmail(), session.getId());

            userLoginInfo.setUsername(userBaseInfo.getUsername());
            userLoginInfo.setLoggedIn(1);
            userLoginInfo.setAccountStatus(2);

            return new BaseVO(200, userLoginInfo, "登录成功！");

        } catch (UnknownAccountException unknownAccountException) {
            // 账户未注册，但之前未判断成功，服务器出现了错误！
            userLoginInfo.setAccountStatus(4);
            LOGGER.error("log in fail, server error");
            return new BaseVO(500, userLoginInfo, "服务器错误！");
        } catch (IncorrectCredentialsException incorrectCredentialsException) {
            // 密码错误
            userLoginInfo.setAccountStatus(2);
            LOGGER.info("log in fail, incorrect credential: email={}, password={}", email, emailPasswordDTO.getPassword());
            return new BaseVO(200, userLoginInfo, "密码！");
        } catch (Exception e) {
            // 服务器出现未知错误
            userLoginInfo.setAccountStatus(4);
            LOGGER.error("unknown error: error message={}", e.getMessage());
            return new BaseVO(200, userLoginInfo, "服务器未知错误！");
        }


    }


    /**
     * 用户登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpSession session) {
        LOGGER.info("try to logout: SESSIONID={}", session.getId());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        LOGGER.info("logout success: SESSIONID={}", session.getId());
    }


    /**
     * 重置用户密码
     * @param emailPasswordDTO 用户修改密码信息，包含email、password。
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public BaseVO resetPassword(@RequestBody EmailPasswordDTO emailPasswordDTO) {
        return new BaseVO(200, true, null);
    }


    /**
     * 检查用户是否登录接口，管理前端受限页面访问。
     * @param session
     * @return true为已登录，false为未登录
     */
    @RequestMapping(value = "/checkLogged", method = RequestMethod.GET)
    @ResponseBody
    public BaseVO checkLogged(HttpSession session) {
        String id = session.getId();
        LOGGER.info("try to check user logged or not: SESSIONID={}", id);

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            // 用户已登录
            LOGGER.info("user logged: SESSIONID={}", id);
            return new BaseVO(200, true,"用户已登录");
        } else {
            // 用户未登录
            LOGGER.info("user does not log in: SESSIONID={}", id);
            return new BaseVO(200, false,"用户未登录");
        }

    }



/*    *//**
     * 获得用户基本信息
     * @param email 邮箱地址
     * @return BaseVO中object成员变量为UserInfoVO
     *//*
    @RequestMapping(value = "/userInfo/{email}", method = RequestMethod.POST)
    @ResponseBody
    public BaseVO getUserInfo(@PathVariable(value = "email") String email) {
        return null;
    }

    */

    /**
     * 用户注销
     * @param id 用户id
     *//*
    @RequestMapping(value = "/logoff/{id}", method = RequestMethod.GET)
    public void logoff(@PathVariable(value = "id") Long id) {

    }*/




}
