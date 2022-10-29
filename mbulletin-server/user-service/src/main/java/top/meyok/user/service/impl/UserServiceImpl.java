package top.meyok.user.service.impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import top.meyok.user.constants.AccountStatusEnum;
import top.meyok.user.constants.ShiroConstants;
import top.meyok.user.mapper.UserDao;
import top.meyok.user.pojo.dto.UserBaseInfoDTO;
import top.meyok.user.pojo.dto.UserCertifyInfoDTO;
import top.meyok.user.pojo.po.UserCertifyInfoDO;
import top.meyok.user.util.SnowflakeWorkerUtils;
import top.meyok.user.pojo.dto.EmailPasswordSaltDTO;
import top.meyok.user.pojo.dto.UserRegisterInfoDTO;
import top.meyok.user.pojo.po.UserPrivacyInfoDO;
import top.meyok.user.service.UserService;
import top.meyok.user.util.RandomCharacterUtils;

import javax.annotation.Resource;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    /**
     * 获取账户状态
     * @param email 邮箱地址
     * @return
     */
    @Override
    public AccountStatusEnum getMailStatus(String email) {

        AccountStatusEnum STATUS;

        UserPrivacyInfoDO userPrivacyInfo = userDao.getUserPrivacyInfoByEmail(email);
        if (ObjectUtils.isEmpty(userPrivacyInfo)) {
            // 未查询到账户，为未注册
            STATUS = AccountStatusEnum.UNREGISTERED;
        } else {
            UserCertifyInfoDO userCertifyInfo = userDao.getUserCertifyInfoByEmail(email);
            if (ObjectUtils.isEmpty(userCertifyInfo)) {
                // 未成功添加认证信息
                STATUS = AccountStatusEnum.ERROR;
            } else {
                if (userCertifyInfo.getCertified() == 0) {
                    // 已经注册，但未激活
                    STATUS = AccountStatusEnum.REGISTERED_BUT_NONACTIVATED;
                } else {
                    // 已经注册且激活
                    STATUS = AccountStatusEnum.REGISTERED_AND_NACTIVATED;
                }

            }

        }

        return STATUS;

    }

    /**
     * 注册用户信息，需要填入user_privacy_info、user_certify_info表，事务相关
     * @param userRegisterInfoDTO
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Boolean registerUserInfo(UserRegisterInfoDTO userRegisterInfoDTO) {

        // 1.填入user_privacy_info表
        // 雪花id
        Long snowflakeId = SnowflakeWorkerUtils.getSnowflakeId();
        // 盐值
        String salt = RandomCharacterUtils.getSalt(32);
        // 密码，前端转来的密码进行加盐、哈希散列处理
        String pwd = new Md5Hash(userRegisterInfoDTO.getPassword(), salt, ShiroConstants.HASH_ITERATION).toHex();

        UserPrivacyInfoDO userPrivacyInfoDO = new UserPrivacyInfoDO();
        userPrivacyInfoDO.setSnowflakeId(snowflakeId);
        userPrivacyInfoDO.setEmail(userRegisterInfoDTO.getEmail());
        userPrivacyInfoDO.setUsername(userRegisterInfoDTO.getUsername());
        userPrivacyInfoDO.setPwd(pwd);
        userPrivacyInfoDO.setSalt(salt);


        // 2.填入user_certify_info表
        // 邮箱激活码
        String checkCode = RandomCharacterUtils.getPictureCheckCode(32);
        UserCertifyInfoDO userCertifyInfoDO = new UserCertifyInfoDO();
        userCertifyInfoDO.setSnowflakeId(snowflakeId);
        userCertifyInfoDO.setCheckCode(checkCode);

        try {
            userDao.saveUserPrivacyInfo(userPrivacyInfoDO);
            userDao.saveUserCertifyInfo(userCertifyInfoDO);

            return true;

        } catch (Exception e) {
            LOGGER.error("insert user info ro user_privacy_info and user_certify_info failed.error info: {}", e.getMessage());
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return false;

    }


    /**
     * 获取账户密码
     * @param email
     * @return
     */
    @Override
    public EmailPasswordSaltDTO getPasswordInfo(String email) {
        // TODO: MeYok 2022/09/02 事务管理
        UserPrivacyInfoDO userPrivacyInfo = userDao.getUserPrivacyInfoByEmail(email);
        EmailPasswordSaltDTO emailPasswordSaltDTO = new EmailPasswordSaltDTO();
        emailPasswordSaltDTO.setEmail(email);
        emailPasswordSaltDTO.setPassword(userPrivacyInfo.getPwd());
        emailPasswordSaltDTO.setSalt(userPrivacyInfo.getSalt());
        return emailPasswordSaltDTO;
    }

    /**
     * 查询用户认证信息
     * @param email
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserCertifyInfoDTO getUserCertifyInfoDTO(String email) {

        try {
            UserCertifyInfoDTO userCertifyInfoDTO = new UserCertifyInfoDTO();
            UserCertifyInfoDO userCertifyInfo = userDao.getUserCertifyInfoByEmail(email);

            // 未查询到相关用户信息
            if (ObjectUtils.isEmpty(userCertifyInfo)) {
                return null;
            }

            userCertifyInfoDTO.setSnowflakeId(userCertifyInfo.getSnowflakeId());
            userCertifyInfoDTO.setCertified(userCertifyInfo.getCertified());
            userCertifyInfoDTO.setCheckCode(userCertifyInfo.getCheckCode());
            return userCertifyInfoDTO;
        } catch (Exception e) {
            LOGGER.error("query user certify info failed.error info: {}", e.getMessage());
            // 手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return null;

    }

    /**
     * 激活账户信息
     * @param email
     */
    @Override
    public void activateUserAccountInfo(String email) {
        LOGGER.info("activate user account: email={}", email);
        userDao.updateCertifiedIsTrueAtUserCertifyInfo(email);
    }


    /**
     * 获取用户基本信息，包含雪花id，邮箱，用户名
     * @param email
     * @return
     */
    @Override
    public UserBaseInfoDTO getUserBaseInfo(String email) {
        UserPrivacyInfoDO userPrivacyInfo = userDao.getUserPrivacyInfoByEmail(email);

        if (ObjectUtils.isEmpty(userPrivacyInfo)) {
            LOGGER.info("account info not found: email={}", email);
            return null;
        }

        UserBaseInfoDTO userBaseInfo = new UserBaseInfoDTO();
        userBaseInfo.setSnowflakeId(userPrivacyInfo.getSnowflakeId());
        userBaseInfo.setEmail(email);
        userBaseInfo.setUsername(userPrivacyInfo.getUsername());

        return userBaseInfo;

    }


}
