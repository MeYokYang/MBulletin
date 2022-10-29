package top.meyok.user.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/10/25 17:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCertifyInfoDO {
    private Long id;
    private Long snowflakeId;
    private Byte certified;
    private String checkCode;
    private Date gmtCreate;
    private Date gmtModified;
}
