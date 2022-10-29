package top.meyok.user.mapper;


import top.meyok.user.pojo.dto.EmailPasswordSaltDTO;
import top.meyok.user.pojo.po.UserCertifyInfoDO;
import top.meyok.user.pojo.po.UserPrivacyInfoDO;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
public interface UserDao {

    void saveUserPrivacyInfo(UserPrivacyInfoDO userPrivacyInfoDO);
    UserPrivacyInfoDO getUserPrivacyInfoByEmail(String email);


    void saveUserCertifyInfo(UserCertifyInfoDO userCertifyInfoDO);
    UserCertifyInfoDO getUserCertifyInfoByEmail(String email);

    void updateCertifiedIsTrueAtUserCertifyInfo(String email);



}
