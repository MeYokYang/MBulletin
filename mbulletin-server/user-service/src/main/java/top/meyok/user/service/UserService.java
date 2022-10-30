package top.meyok.user.service;

import top.meyok.user.constant.AccountStatusEnum;
import top.meyok.user.pojo.dto.EmailPasswordSaltDTO;
import top.meyok.user.pojo.dto.UserBaseInfoDTO;
import top.meyok.user.pojo.dto.UserCertifyInfoDTO;
import top.meyok.user.pojo.dto.UserRegisterInfoDTO;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
public interface UserService {

    /**
     * 查询邮箱状态
     * @param email 邮箱地址
     * @return 为空则邮箱未注册
     */
    AccountStatusEnum getMailStatus(String email);

    Boolean registerUserInfo(UserRegisterInfoDTO userRegisterInfoDTO);

    EmailPasswordSaltDTO getPasswordInfo(String email);

    UserCertifyInfoDTO getUserCertifyInfoDTO(String email);


    void activateUserAccountInfo(String email);

    UserBaseInfoDTO getUserBaseInfo(String email);

}
