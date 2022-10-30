package top.meyok.user.mapper;


import top.meyok.user.pojo.po.UserCertifyDO;
import top.meyok.user.pojo.po.UserPrivacyDO;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
public interface UserDao {

    void saveUserPrivacy(UserPrivacyDO userPrivacyDO);
    UserPrivacyDO getUserPrivacyByEmail(String email);


    void saveUserCertify(UserCertifyDO userCertifyDO);
    UserCertifyDO getUserCertifyByEmail(String email);

    void updateCertifiedIsTrueAtUserCertify(String email);



}
