package tyml.reservationshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminHome() {
        log.info("adminController IN!!");
        return "/admin/adminPage";
    }

}
