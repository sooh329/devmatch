package com.dongyang.devmatch.service;

import com.dongyang.devmatch.entity.ProjectPost;
import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.repository.ProjectPostRepository;
import com.dongyang.devmatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;
    private final UserRepository userRepository;

    public List<ProjectPost> findAll() {
        return projectPostRepository.findAll();
    }

    public Optional<ProjectPost> findById(Long id) {
        return projectPostRepository.findById(id);
    }

    public ProjectPost create(ProjectPost post, User user) {
        post.setAuthor(user);
        return projectPostRepository.save(post);
    }

    public ProjectPost update(ProjectPost existingPost, ProjectPost updatedData) {
        existingPost.setTitle(updatedData.getTitle());
        existingPost.setContent(updatedData.getContent());
        return projectPostRepository.save(existingPost);
    }

    public void delete(ProjectPost post) {
        projectPostRepository.delete(post);
    }

    public List<ProjectPost> findByAuthorUsername(String username) {
        return projectPostRepository.findByAuthorUsername(username);
    }

}
