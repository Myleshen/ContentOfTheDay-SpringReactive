package dev.myleshenp.contentnotification.content;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
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
  Mono<Content> addContent(@RequestBody Content content) {
    return contentService.addContent(content);
  }

  @GetMapping("/cod/emails")
  Mono<String> sendContentOfTheDayEmail(@RequestBody String destinationEmail) {
    if (contentService.sendTodayContentAsEmail(destinationEmail)) {
      return Mono.just("Successfully sent COTD");
    }
    return Mono.just("Issue in sending COTD");
  }

  @GetMapping("/random")
  Flux<Content> getRandomContent() {
    return contentService.getRandomContent(1);
  }

  @GetMapping("/random/{size}")
  Flux<Content> getRandomContent(@PathVariable int size) {
    return contentService.getRandomContent(size);
  }
}
