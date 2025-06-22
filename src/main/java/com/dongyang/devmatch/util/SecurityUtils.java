package com.dongyang.devmatch.util;

import com.dongyang.devmatch.entity.ProjectPost;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // 인증된 사용자 이름을 반환
    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        return authentication.getName();
    }

    // 게시글의 작성자인지 확인
    public static void validateOwnership(ProjectPost post, String username) {
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }
    }
}
