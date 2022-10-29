package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/9/2 13:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailPasswordSaltDTO {
    private String email;
    private String password;
    private String salt;
}
