package top.meyok.user.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 7:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseVO {
    /**
     * HTTP状态码
     */
    private int status;
    /**
     * 返回内容
     */
    private Object object;
    /**
     * 描述消息
     */
    private String message;

}
