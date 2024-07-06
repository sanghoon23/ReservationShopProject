package tyml.reservationshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tyml.reservationshop.domain.dto.EmailAuthDto;
import tyml.reservationshop.domain.dto.EmailSenderDto;
import tyml.reservationshop.service.MailSendService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailSendService mailSendService;

    @PostMapping("/sendAuthEmail")
    public ResponseEntity<String> sendAuthEmail(@Valid EmailSenderDto emailDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>("이메일이 정확히 입력되어있는지 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
        try {
            mailSendService.sendEmail(emailDto.getEmail());
            return new ResponseEntity<>("Email sent successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error sending email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authCheck")
    public ResponseEntity<String> authCheck(@Valid EmailAuthDto emailAuthDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>("인증번호를 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        if(mailSendService.correctAuthNum(emailAuthDto.getEmail(), emailAuthDto.getAuthNumber()))
        {
            return new ResponseEntity<>("인증완료", HttpStatus.OK);
        }

        return new ResponseEntity<>("인증번호가 틀렸어요.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
