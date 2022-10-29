package top.meyok.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDTO {
    private String[] tos;
    private String subject;
    private String content;
    private String[] filePaths;
}
