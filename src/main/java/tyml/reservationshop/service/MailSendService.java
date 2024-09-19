package tyml.reservationshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tyml.reservationshop.service.util.UtilRedis;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class MailSendService {


    private final JavaMailSender mailSender;
    private final UtilRedis utilRedis;
    private String authNumber;

    public void RandomAuthNum() {
        Random random = new Random();

        StringBuilder makingNum = new StringBuilder();
        for(int i = 0; i < 6; ++i)
        {
            makingNum.append(random.nextInt(10));
        }
        authNumber = makingNum.toString();

    }

    public boolean correctAuthNum(String email, String authNumber) {

        String a = utilRedis.getDate(email);

        return Optional.ofNullable(utilRedis.getDate(email))
                .map(date -> date.equals(authNumber))  // 값이 존재할 경우 비교
                .orElse(false);  // 값이 없으면 false 반환

    }

    public String sendEmail(String email) {

        RandomAuthNum();
        String organizer = "sanghoon2323@gmail.com";
        String toMail = email;
        String title = "회원 가입 인증 이메일";
        String content =
                        "회원 가입 인증 번호는 아래입니다." +
                        "<br><br>" +
                        "인증 번호 : " + authNumber + " 입니다."+
                        "<br><br>" +
                        "감사합니다.";

        sendProcess(organizer, toMail, title, content);
        return authNumber;
    }

    private void sendProcess(String org, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(org);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);

            //redis 인증번호 저장
            utilRedis.setDataAndTime(toMail, authNumber, 60*5L);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", toMail, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
