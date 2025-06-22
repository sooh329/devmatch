package com.dongyang.devmatch.api;

import com.dongyang.devmatch.entity.ProjectPost;
import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.service.ProjectPostService;
import com.dongyang.devmatch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import static com.dongyang.devmatch.util.SecurityUtils.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectPostApiController {

    private final ProjectPostService projectPostService;
    private final UserService userService; // 사용자 조회용 서비스

    @GetMapping
    public ResponseEntity<List<ProjectPost>> list() {
        List<ProjectPost> posts = projectPostService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectPost> detail(@PathVariable Long id) {
        ProjectPost post = projectPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<ProjectPost> create(@RequestBody ProjectPost projectPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        User loginUser = userService.findByUsername(username);

        ProjectPost created = projectPostService.create(projectPost, loginUser);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectPost> update(@PathVariable Long id, @RequestBody ProjectPost projectPost) {
        String username;
        try {
            username = getAuthenticatedUsername();
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }

        ProjectPost existingPost = projectPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        try {
            validateOwnership(existingPost, username);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }

        ProjectPost updated = projectPostService.update(existingPost, projectPost);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String username;
        try {
            username = getAuthenticatedUsername();
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }

        ProjectPost post = projectPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        try {
            validateOwnership(post, username);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }

        projectPostService.delete(post);
        return ResponseEntity.noContent().build();
    }
}
