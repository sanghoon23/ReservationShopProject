package tyml.reservationshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import tyml.reservationshop.service.util.UrlUtils;

import java.io.IOException;
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
        PlaceForm placeForm = new PlaceForm(place);

        return ResponseEntity.ok(placeForm);
    }

    @GetMapping("/place/commentList/{placeId}")
    public ResponseEntity<List<CommentDto>> getPlaceCommentList(@PathVariable("placeId") Long placeId,
                                                                @AuthenticationPrincipal User user) {

        final Long currentUserId;

        if (user != null) {
            String userEmail = user.getUsername();
            Member member = memberService.findByEmail(userEmail);
            currentUserId = member.getId();
        }
        else{
            currentUserId = -1L;
        }


        List<Comment> commentList = commentService.findByPlaceId(placeId);

        List<CommentDto> commentDtos = commentList.stream()
                .map( comment -> CommentDto.from(comment, currentUserId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentDtos);
    }

    @PostMapping("/place/comment/add/{placeId}")
    public ResponseEntity<CommentDto> addCommentToPlace(@PathVariable Long placeId,
                                                        @AuthenticationPrincipal User user,
                                                        HttpServletResponse response,
                                                        HttpServletRequest request,
                                                        @RequestParam String content) throws IOException {


        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized 응답
        }

        String userEmail = user.getUsername();
        Member member = memberService.findByEmail(userEmail);
        Long currentUserId = member.getId();
        String userName = member.getName();

        Place place = placeService.findOne(placeId);

        Comment comment = new Comment(content, currentUserId, userName, place);
        commentService.join(comment);

        CommentDto commentDto = CommentDto.from(comment, currentUserId);
        return ResponseEntity.ok(commentDto);
    }

    @PutMapping("/place/{placeId}/comment/update/{commentId}")
    public ResponseEntity<Void> updateCommentInPlace(@PathVariable Long placeId,
                                                     @PathVariable Long commentId,
                                                     @RequestParam String content) {

        commentService.updateComment(commentId, content);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/place/{placeId}/comment/delete/{commentId}")
    public ResponseEntity<Void> deleteCommentInPlace(@PathVariable Long placeId,
                                                     @PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


}
