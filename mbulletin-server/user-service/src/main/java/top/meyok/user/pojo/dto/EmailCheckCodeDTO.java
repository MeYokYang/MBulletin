package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/10/25 20:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckCodeDTO {
    private String email;
    private String checkCode;
}
