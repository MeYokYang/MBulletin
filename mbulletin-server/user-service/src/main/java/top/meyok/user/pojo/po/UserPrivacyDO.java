package top.meyok.user.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivacyDO {

    private Long id;
    private Long snowflakeId;
    private String email;
    private String username;
    private String pwd;
    private String salt;
    private Date gmtCreate;
    private Date gmtModified;

}
