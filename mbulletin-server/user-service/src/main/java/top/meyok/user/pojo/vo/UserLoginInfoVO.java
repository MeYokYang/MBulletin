package top.meyok.user.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/10/26 15:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginInfoVO {

    private String email;
    private String username;
    private Integer loggedIn;
    private Integer accountStatus;



}
