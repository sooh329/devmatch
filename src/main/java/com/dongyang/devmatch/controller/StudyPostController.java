package com.dongyang.devmatch.controller;

import com.dongyang.devmatch.entity.StudyPost;
import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.service.StudyPostService;
import com.dongyang.devmatch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyPostController {

    private final StudyPostService studyPostService;
    private final UserService userService;

    // 게시글 목록 페이지
    @GetMapping
    public String list(Model model) {
        List<StudyPost> posts = studyPostService.findAll();
        model.addAttribute("posts", posts);
        return "study/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        StudyPost post = studyPostService.findById(id)
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

        return "study/detail";
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String newPostForm(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        return "study/new";
    }

    // 게시글 작성 처리
    @PostMapping("/new")
    public String create(@ModelAttribute StudyPost studyPost,
                         @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(userDetails.getUsername());

        studyPostService.create(studyPost, user);

        return "redirect:/study";
    }

    // 게시글 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        StudyPost post = studyPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/study/" + id;
        }

        model.addAttribute("post", post);
        return "study/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @ModelAttribute StudyPost studyPost,
                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        StudyPost existingPost = studyPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!existingPost.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/study/" + id;
        }

        studyPostService.update(existingPost, studyPost);
        return "redirect:/study/" + id;
    }

    // 게시글 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        StudyPost post = studyPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/study/" + id;
        }

        studyPostService.delete(post);
        return "redirect:/study";
    }
}
