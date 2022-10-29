package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/9/2 12:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailPasswordDTO {
    private String email;
    private String password;
}
