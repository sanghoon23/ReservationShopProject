package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.EmailSenderDto;
import tyml.reservationshop.domain.dto.ItemModifyForm;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.service.ItemService;
import tyml.reservationshop.service.PlaceService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final ItemService itemService;

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
                                  @RequestParam(value = "cancel", required = false) String cancel,
                                  @RequestParam(value = "mainImage", required = false) MultipartFile image,
                                  @RequestParam(value = "productName[]", required = false) List<String> productNames,
                                  @RequestParam(value = "productPrice[]", required = false) List<Integer> productPrices,
                                  @RequestParam(value = "productImage[]", required = false) List<MultipartFile> productImages,
                                  Model model) {

        if ("true".equals(cancel)) {
            return "redirect:/admin/placeList";
        }

        if (bindingResult.hasErrors()) {
            return "/place/createPlaceForm";
        }

        // 이미지 삽입
        if (!image.isEmpty()) {
            placeForm.setUploadImageFileName(saveImage(image));
        }

        List<Item> itemList = new ArrayList<>();
        Place place = new Place(placeForm);
        placeService.join(place);
        for (int i = 0; i < productNames.size(); ++i) {
            Item item = Item.builder()
                    .itemName(productNames.get(i))
                    .price(productPrices.get(i).longValue())
                    .uploadImageFileName(saveImage(productImages.get(i)))
                    .place(place) // set place
                    .build();

            itemList.add(item);
        }
        //@한번의 쿼리
        itemService.saveAll(itemList);
        placeService.addAllItems(place.getId(), itemList);

        return "redirect:/admin/placeList";
    }

    @GetMapping("/place/placeList")
    public String placeList(Model model) {
        List<Place> placeList = placeService.findAll();
        model.addAttribute("places", placeList);

        return "/place/placeList";
    }

    @GetMapping("/place/detail/{placeId}")
    public String placeInfo(@PathVariable("placeId") Long placeId, Model model) {

        model.addAttribute("place", placeService.findOne(placeId));
        return "/place/detail/placeInfo";
    }

    @GetMapping("/place/modifyPlaceForm/{placeId}")
    public String modifyPlaceForm(@PathVariable("placeId") Long placeId, HttpSession session, Model model) {

        model.addAttribute("placeId", placeId);

        Place place = placeService.findOne(placeId);
        model.addAttribute("placeForm", new PlaceForm(place));
        model.addAttribute("placeItems", place.getItems());

        return "/place/modifyPlaceForm";
    }

    @PostMapping("/place/modifyPlaceForm/{placeId}")
    public String modifyPlaceForm(@PathVariable("placeId") Long placeId,
                                  @RequestParam(value = "cancel", required = false) String cancel,
                                  @Valid PlaceForm placeForm,
                                  BindingResult bindingResult,
                                  @RequestParam("mainImage") MultipartFile image,
                                  @RequestParam(value = "productName[]", required = false) List<String> productNames,
                                  @RequestParam(value = "productPrice[]", required = false) List<Integer> productPrices,
                                  @RequestParam(value = "productImage[]", required = false) List<MultipartFile> productImages,
                                  @RequestParam Map<String, String> params,
                                  @RequestParam Map<String, MultipartFile> imageParams) {

        if ("true".equals(cancel)) {
            return "redirect:/admin/placeList";
        }

        if (bindingResult.hasErrors()) {
            return "/place/modifyPlaceForm";
        }

        Place findPlace = placeService.findOne(placeId);

        //@1.메인 이미지 수정
        if (!image.isEmpty()) {
            if (findPlace.getUploadImageFileName() != null && !findPlace.getUploadImageFileName().isEmpty()) {
                deleteImage(findPlace.getUploadImageFileName());
            }

            placeForm.setUploadImageFileName(saveImage(image));
        } else {
            placeForm.setUploadImageFileName(findPlace.getUploadImageFileName());
        }
        placeService.updatePlace(placeId, placeForm);


        List<Item> addingItemList = new ArrayList<>();

        //@2.기존 Place item Update
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.startsWith("delete_")) {
                Long itemId = Long.parseLong(key.substring("delete_".length()));
                boolean isDeleted = "true".equals(params.get("delete_" + itemId));
                if (isDeleted) {
                    Item findItem = itemService.findOne(itemId);
                    //@이미지 제거
                    if(findItem.getUploadImageFileName() != null && !findItem.getUploadImageFileName().isEmpty()){

                    deleteImage(findItem.getUploadImageFileName());
                    }
                    itemService.deleteItem(itemId);
                    placeService.deleteItemInPlaceItemList(placeId, findItem);
                    continue;
                }
            }

            if (key.startsWith("itemName_")) {
                Long itemId = Long.parseLong(key.substring("itemName_".length()));

                String itemName = params.get("itemName_" + itemId);
                String price = params.get("price_" + itemId);
                String imageFileName = "";

                //@비어있지않을 때,
                if (!(imageParams.get("newImage_" + itemId).isEmpty())) {
                    imageFileName = saveImage(imageParams.get("newImage_" + itemId));
                } else {
                    imageFileName = params.get("uploadImageFileName_" + itemId);
                }

                ItemModifyForm itemModifyForm = new ItemModifyForm();
                itemModifyForm.setItemName(itemName);
                itemModifyForm.setPrice(Long.parseLong(price));
                itemModifyForm.setUploadImageFileName(imageFileName);

                itemService.updateItem(itemId, itemModifyForm);
            }//@if
        }

        //@3.새로운 Place Item 추가
        if (productNames != null) {
            for (int i = 0; i < productNames.size(); ++i) {

                String InputUploadImageFileName = "";
                if(productImages.get(i) != null && !productImages.get(i).isEmpty()){
                    InputUploadImageFileName = saveImage(productImages.get(i));
                }

                Item item = Item.builder()
                        .itemName(productNames.get(i))
                        .price(productPrices.get(i).longValue())
                        .uploadImageFileName(InputUploadImageFileName)
                        .place(findPlace)
                        .build();

                addingItemList.add(item);
            }
        }

        //@한번의 쿼리
        itemService.saveAll(addingItemList);
        placeService.addAllItems(placeId, addingItemList);

        return "redirect:/admin/placeList";
    }

    @GetMapping("/place/deletePlace/{placeId}")
    public String deletePlace(@PathVariable("placeId") Long placeId) {

        placeService.deletePlace(placeId);
        return "redirect:/admin/placeList";
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

    private void deleteImage(String fileName) {
        try {
            Path filePath = Paths.get(potoUploadPath, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + fileName, e);
        }
    }

//    public MultipartFile getMultipartFileFromSavedImage(String fileName) {
//        try {
//            // 저장된 파일 경로
//            String uploadDir = potoUploadPath;
//            File file = new File(uploadDir, fileName);
//
//            // 원래 파일 이름 추출
//            String originalFileName = fileName.substring(fileName.indexOf('_') + 1);
//
//            // 파일을 읽어서 byte 배열로 변환
//            FileInputStream input = new FileInputStream(file);
//            byte[] content = FileCopyUtils.copyToByteArray(input);
//
//            // MockMultipartFile을 이용하여 MultipartFile로 변환
//            MultipartFile multipartFile = new MockMultipartFile(
//                    "file",             // 필드명
//                    originalFileName,   // 원본 파일명
//                    "image/jpeg",       // MIME 타입 (파일 확장자에 맞게 변경 가능)
//                    content             // 파일 내용
//            );
//
//            return multipartFile;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to convert file to MultipartFile", e);
//        }
//    }

}
