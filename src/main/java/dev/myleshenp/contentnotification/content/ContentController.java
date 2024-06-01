package dev.myleshenp.contentnotification.content;

import static dev.myleshenp.contentnotification.constants.ApplicationConstants.CONTENT_SIZE_FOR_NOTIFICATIONS;

import dev.myleshenp.contentnotification.notification.email.EmailRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Slf4j
public class ContentController {

    final ContentService contentService;

    @GetMapping
    Flux<Content> getAllContent() {
        return contentService.getAllContent();
    }

    @GetMapping("/{id}")
    Mono<Content> getContentById(@PathVariable String id) {
        return contentService.getById(id);
    }

    @PostMapping
    Mono<Content> addContent(
            @Valid @RequestBody Content content, @AuthenticationPrincipal Jwt jwt) {
        content = content.withUserName(jwt.getClaims().get("preferred_username").toString());
        return contentService.addContent(content);
    }

    @GetMapping("/cod/emails")
    String sendContentOfTheDayEmail(@Valid @RequestBody EmailRequest emailRequest) {
        return contentService.sendTodayContentAsEmail(emailRequest.to());
    }

    @GetMapping("/random")
    Flux<Content> getRandomContent() {
        return contentService.getRandomContent(CONTENT_SIZE_FOR_NOTIFICATIONS);
    }

    @GetMapping("/random/{size}")
    Flux<Content> getRandomContent(@PathVariable int size, @AuthenticationPrincipal Jwt jwt) {
        return contentService.getRandomContent(size, jwt.getClaimAsString("preferred_username"));
    }
}
