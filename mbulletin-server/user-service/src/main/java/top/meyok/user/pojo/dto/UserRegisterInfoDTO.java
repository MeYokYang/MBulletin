package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 21:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterInfoDTO {
    private String email;
    private String username;
    private String password;
}
