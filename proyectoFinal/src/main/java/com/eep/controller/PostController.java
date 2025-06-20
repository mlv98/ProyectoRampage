package com.eep.controller;

import com.eep.dto.PostDto;
import com.eep.service.PostService;
import jakarta.validation.Valid;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String list(
      @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable,
      Model model,
      Principal principal) {

        model.addAttribute("posts", postService.listAll(pageable));
        model.addAttribute("userName", principal.getName());
        return "posts/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "posts/new";
    }

    @PostMapping
    public String create(
        @Valid @ModelAttribute PostDto dto,
        BindingResult br,
        @RequestParam(name="files", required = false) List<MultipartFile> files,
        Principal principal
    ) {
      if (br.hasErrors()) {
        return "posts/new";
      }
      postService.create(dto, principal.getName(), files == null ? List.of() : files);
      return "redirect:/posts";
    }

    @RequestMapping(
      value = "/{id}/edit",
      method = { RequestMethod.PUT, RequestMethod.POST }
    )
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute PostDto dto,
        BindingResult br,
        Principal principal
    ) {
      if (br.hasErrors()) {
        return "redirect:/posts";
      }
      postService.update(id, dto, principal.getName());
      return "redirect:/posts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        postService.delete(id, principal.getName());
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    @ResponseBody
    public Map<String,Integer> like(@PathVariable Long id, Principal principal) {
      int count = postService.toggleLike(id, principal.getName());
      return Collections.singletonMap("count", count);
    }

    
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
      return new HiddenHttpMethodFilter();
    }
    
    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                Principal principal) {
       
        postService.deleteComment(postId, commentId, principal.getName());
       
        return "redirect:/posts";
    }

}
