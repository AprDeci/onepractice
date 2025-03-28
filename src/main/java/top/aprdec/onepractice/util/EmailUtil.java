package top.aprdec.onepractice.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.eenum.EmailTemplateEnum;

import java.io.UnsupportedEncodingException;

@Component
public class EmailUtil {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String to, String content,String subject){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
            helper.setFrom("1263868407@qq.com", "onepractice");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("创建邮件失败");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("邮件编码不支持");
        } catch (Exception e) {
            throw new RuntimeException("邮件发送错误"+e.getMessage());
        }
    }
}
