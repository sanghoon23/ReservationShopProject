package tyml.reservationshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import tyml.reservationshop.domain.Comment;
import tyml.reservationshop.domain.Member;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.CommentDto;
import tyml.reservationshop.domain.dto.PlaceForm;
import tyml.reservationshop.service.CommentService;
import tyml.reservationshop.service.PlaceService;
import tyml.reservationshop.service.user.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceRestController {

    private final PlaceService placeService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/place/map/detail/{placeId}")
    public ResponseEntity<PlaceForm> getPlaceDetail(@PathVariable("placeId") Long placeId) {
        Place place = placeService.findOne(placeId);

        log.info("place GetPlaceDeatil!!");

        PlaceForm placeForm = new PlaceForm(place);

        return ResponseEntity.ok(placeForm);
    }

    @GetMapping("/place/commentList/{placeId}")
    public ResponseEntity<List<CommentDto>> getPlaceCommentList(@PathVariable("placeId") Long placeId) {

        List<Comment> commentList = commentService.findByPlaceId(placeId);

        List<CommentDto> commentDtos = commentList.stream()
                .map(CommentDto::from).collect(Collectors.toList());
        return ResponseEntity.ok(commentDtos);
    }

    @PostMapping("/place/commentList/{placeId}")
    public ResponseEntity<CommentDto> addCommentToPlace(@PathVariable Long placeId,
                                                        @AuthenticationPrincipal User user,
                                                        @RequestParam String content) {
        //현재 로그인된 Member 의 userId 구하기
        String userEmail = user.getUsername();

        Member member = memberService.findByEmail(userEmail);
        Long userId = member.getId();

        Place place = placeService.findOne(placeId);

        Comment comment = new Comment(content, userId, place);
        commentService.join(comment);

        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getUpdateAt());

        return ResponseEntity.ok(commentDto);
    }

}
