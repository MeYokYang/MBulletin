package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/10/25 18:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCertifyInfoDTO {
    private Long snowflakeId;
    private Byte certified;
    private String checkCode;
}
