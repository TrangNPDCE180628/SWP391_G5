package Ultis;


import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtility {

    // Gửi email với nội dung đơn giản (plain text)
    public static void sendEmail(String toEmail, String subject, String messageContent)
            throws MessagingException, UnsupportedEncodingException {
        
        // Cấu hình thông tin SMTP  
        final String fromEmail = "DuanNTCE171842@fpt.edu.vn";           // Gmail của bạn
        final String appPassword = "xfkw oghu hdkq cjuk";            // App password (bắt buộc dùng khi bật 2FA)

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");         
        // Server gửi mail
        props.put("mail.smtp.port", "587");                        // Port hỗ trợ TLS
        props.put("mail.smtp.auth", "true");                       // Bắt buộc xác thực
        props.put("mail.smtp.starttls.enable", "true");            // Bật TLS

        // Xác thực tài khoản gửi
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        };

        // Tạo session gửi mail
        Session session = Session.getInstance(props, auth);

        // Tạo message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail, "Support Team")); // Tên người gửi hiển thị
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        msg.setContent(messageContent, "text/html; charset=utf-8");
        msg.setSubject(subject);
        

        // Gửi email
        Transport.send(msg);

        System.out.println("Email sent successfully to " + toEmail);
    }
}
