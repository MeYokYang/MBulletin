package top.meyok.user.service;

import top.meyok.user.pojo.dto.MailDTO;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
public interface MailService {
    /**
     * 向目标邮箱发送简单的文本内容
     * @param mail 封装好的发送目标邮件内容，需要目标邮件地址、主题、内容、附件。
     */
    void sendSimpleMail(MailDTO mail);

    /**
     * 向目标邮箱发送需要解析html标签的内容
     * @param mail 封装好的发送目标邮件内容，需要目标邮件地址、主题、内容。
     */
    void sendHtmlMail(MailDTO mail);

    /**
     * 向目标邮箱发送带附件的，可以解析html标签的内容。
     * @param mail 封装好的发送目标邮件内容，需要目标邮件地址、主题、内容、附件。
     */
    void sendAttachMail(MailDTO mail);
}
