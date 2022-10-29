package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/10/26 16:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseInfoDTO {
    private Long snowflakeId;
    private String email;
    private String username;
}
