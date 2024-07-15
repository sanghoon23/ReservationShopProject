package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.EmailSenderDto;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.service.PlaceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Value("${file.add}")
    private String potoUploadPath;

    @ModelAttribute("categories")
    public List<String> categories() {
        return Arrays.asList("뷰티", "식당카페", "숙박", "공연전시");
    }

    @GetMapping("/place/createPlaceForm")
    public String createPlaceForm(Model model) {

        model.addAttribute("placeForm", new PlaceForm());
        return "/place/createPlaceForm";
    }

    @PostMapping("/place/createPlaceForm")
    public String createPlaceForm(@Valid PlaceForm placeForm,
                                  BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile image,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            return "/place/createPlaceForm";
        }

        // 이미지 삽입
        if (!image.isEmpty()) {
            placeForm.setUploadImageFileName(saveImage(image));
        }
        Place place = new Place(placeForm);
        Long placeId = placeService.join(place);

        return "home";
    }

    //@return : 이미지 파일 이름
    private String saveImage(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            String uploadDir = potoUploadPath;

            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + image.getOriginalFilename(), e);
        }
    }

    @GetMapping("/place/placeList")
    public String placeList(Model model) {
        model.addAttribute("places", placeService.findAll());
        return "/place/placeList";
    }

    @GetMapping("/place/detail/{placeId}")
    public String placeInfo(@PathVariable("placeId") Long placeId,  Model model) {

        model.addAttribute("place", placeService.findOne(placeId));
        return "/place/detail/placeInfo";
    }

    @GetMapping("/place/modifyPlaceForm/{placeId}")
    public String modifyPlaceForm(@PathVariable("placeId") Long placeId, HttpSession session,  Model model) {

        model.addAttribute("placeId", placeId);

        Place place = placeService.findOne(placeId);

        model.addAttribute("placeForm",
                new PlaceForm(place));

        return "/place/modifyPlaceForm";
    }

    @PostMapping("/place/modifyPlaceForm/{placeId}")
    public String modifyPlaceForm(@PathVariable("placeId") Long placeId, @Valid PlaceForm placeForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/place/modifyPlaceForm";
        }

        //TOdo : update

        return "redirect:/place/placeList";
    }

}
