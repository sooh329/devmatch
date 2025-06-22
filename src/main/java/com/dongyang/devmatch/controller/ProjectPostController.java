package com.dongyang.devmatch.controller;

import com.dongyang.devmatch.entity.ProjectPost;
import com.dongyang.devmatch.service.ProjectPostService;
import com.dongyang.devmatch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectPostController {

    private final ProjectPostService projectPostService;
    private final UserService userService;

    // 게시글 목록 페이지
    @GetMapping
    public String list(Model model) {
        List<ProjectPost> posts = projectPostService.findAll();
        model.addAttribute("posts", posts);
        return "project/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        ProjectPost post = projectPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        model.addAttribute("post", post);

        if (userDetails != null) {
            String username = userDetails.getUsername();
            boolean isAuthor = post.getAuthor().getUsername().equals(username);
            model.addAttribute("loginUser", userDetails);
            model.addAttribute("isAuthor", isAuthor);
        } else {
            model.addAttribute("loginUser", null);
            model.addAttribute("isAuthor", false);
        }

        return "project/detail";
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String newPostForm(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "project/new";
    }

    // 게시글 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        ProjectPost post = projectPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/project/" + id;
        }

        model.addAttribute("post", post);
        return "project/edit";
    }
}
