package com.dongyang.devmatch.controller;

import com.dongyang.devmatch.entity.StudyPost;
import com.dongyang.devmatch.entity.ProjectPost;
import com.dongyang.devmatch.service.StudyPostService;
import com.dongyang.devmatch.service.ProjectPostService;
import com.dongyang.devmatch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final StudyPostService studyPostService;
    private final ProjectPostService projectPostService;
    private final UserService userService;

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String username = userDetails.getUsername();

        List<StudyPost> myStudyPosts = studyPostService.findByAuthorUsername(username);
        List<ProjectPost> myProjectPosts = projectPostService.findByAuthorUsername(username);

        model.addAttribute("myStudyPosts", myStudyPosts);
        model.addAttribute("myProjectPosts", myProjectPosts);

        return "mypage/index";
    }

    @PostMapping("/mypage/delete")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        userService.deleteByUsername(userDetails.getUsername());

        return "redirect:/logout"; // 로그아웃 처리
    }

}
