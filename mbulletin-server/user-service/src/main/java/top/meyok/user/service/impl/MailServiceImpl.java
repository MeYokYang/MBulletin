package top.meyok.user.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import top.meyok.user.constant.CompanyInfoConsts;
import top.meyok.user.pojo.dto.MailDTO;
import top.meyok.user.service.MailService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author meyok@meyok.org
 * @date 2022/8/29 3:17
 */
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Override
    public void sendSimpleMail(MailDTO mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from + "(" + CompanyInfoConsts.COMPANY_NAME + ")");
        simpleMailMessage.setSubject(mail.getSubject());
        simpleMailMessage.setText(mail.getContent());

        String[] tos = mail.getTos();
        for (String to : tos) {
            simpleMailMessage.setTo(to);
            javaMailSender.send(simpleMailMessage);
        }

    }

    @Override
    public void sendHtmlMail(MailDTO mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from + "(" + CompanyInfoConsts.COMPANY_NAME + ")");
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getContent(), true);

            String[] tos = mail.getTos();
            for (String to : tos) {
                mimeMessageHelper.setTo(to);
                javaMailSender.send(mimeMessage);
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendAttachMail(MailDTO mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from + "(" + CompanyInfoConsts.COMPANY_NAME + ")");
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getContent(), true);

            String[] filePaths = mail.getFilePaths();
            for (String filePath : filePaths) {
                FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
                String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                try {
                    mimeMessageHelper.addAttachment(fileName, fileSystemResource);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }

            String[] tos = mail.getTos();
            for (String to : tos) {
                mimeMessageHelper.setTo(to);
                javaMailSender.send(mimeMessage);
            }


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
