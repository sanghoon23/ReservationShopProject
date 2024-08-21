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
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.EmailSenderDto;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.service.ItemService;
import tyml.reservationshop.service.PlaceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService  placeService;
    private final ItemService   itemService;

    @Value("${file.add}")
    private String potoUploadPath;

        /*
****************************************************************************************************
    @category
     */

    @ModelAttribute("categories")
    public List<String> categories() {
        return Arrays.asList("뷰티", "식당카페", "숙박", "공연전시");
    }

    @GetMapping("/category/{categoryName}")
    public String categoryList(@PathVariable("categoryName") String categoryName, Model model) {

        List<Place> placeList = placeService.findByCategory(categoryName);

        model.addAttribute("places", placeList);
        model.addAttribute("categoryName", categoryName);

        return "/place/categoryPlaceList";
    }

    /*
****************************************************************************************************
    @Place
     */

    @GetMapping("/place/createPlaceForm")
    public String createPlaceForm(Model model) {

        model.addAttribute("placeForm", new PlaceForm());
        return "/place/createPlaceForm";
    }

    @PostMapping("/place/createPlaceForm")
    public String createPlaceForm(@Valid PlaceForm placeForm,
                                  BindingResult bindingResult,
                                  @RequestParam("mainImage") MultipartFile image,
                                  @RequestParam("productName[]") List<String> productNames,
                                  @RequestParam("productPrice[]") List<Integer> productPrices,
                                  @RequestParam("productImage[]") List<MultipartFile> productImages,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            return "/place/createPlaceForm";
        }

        // 이미지 삽입
        if (!image.isEmpty()) {
            placeForm.setUploadImageFileName(saveImage(image));
        }

        Place place = new Place(placeForm);
        placeService.join(place);
        for (int i = 0; i < productNames.size(); ++i) {
            Item item = Item.builder()
                    .itemName(productNames.get(i))
                    .price(productPrices.get(i).longValue())
                    .uploadImageFileName(saveImage(productImages.get(i)))
                    .place(place) // set place
                    .build();

            // item 저장
            itemService.join(item);

            // Place 에 item 저장
            placeService.addItem(place.getId(), item);
        }

        return "home";
    }

    @GetMapping("/place/placeList")
    public String placeList(Model model) {
        List<Place> placeList = placeService.findAll();
        model.addAttribute("places", placeList);

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
        model.addAttribute("placeForm", new PlaceForm(place));

        return "/place/modifyPlaceForm";
    }

    @PostMapping("/place/modifyPlaceForm/{placeId}")
    public String modifyPlaceForm(@PathVariable("placeId") Long placeId,
                                  @RequestParam(value = "cancel", required = false) String cancel,
                                  @Valid PlaceForm placeForm,
                                  BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile image) {

        if ("true".equals(cancel)) {
            return "redirect:/place/placeList";
        }

        if (bindingResult.hasErrors()) {
            return "/place/modifyPlaceForm";
        }

        // 이미지 삽입
        if (!image.isEmpty()) {
            placeForm.setUploadImageFileName(saveImage(image));
        }

        placeService.updatePlace(placeId, placeForm);

        return "redirect:/place/placeList";
    }

    @GetMapping("/place/deletePlace/{placeId}")
    public String deletePlace(@PathVariable("placeId") Long placeId) {

        placeService.deletePlace(placeId);
        return "redirect:/place/placeList";
    }

    /*
****************************************************************************************************
    @Image
     */

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

}
