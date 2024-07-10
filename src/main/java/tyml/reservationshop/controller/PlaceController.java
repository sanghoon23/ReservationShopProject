package tyml.reservationshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tyml.reservationshop.domain.Place;
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
            placeForm.setImagePath(saveImage(image));
        }
        Place place = new Place(placeForm);
        Long placeId = placeService.join(place);

        return "home";
    }

    private String saveImage(MultipartFile image) {
        try {
            // Create a unique file name
            String originalFilename = image.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            // Specify the path to save the file
            String uploadDir = "D:\\_Dev\\Lecture\\Java\\IntelliJ\\Spring\\Portfolio\\reservationShop\\src\\main\\resources\\static\\images\\upload";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // Save the file
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

//            return filePath.toString();
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

}
