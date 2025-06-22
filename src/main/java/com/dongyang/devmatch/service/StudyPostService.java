package com.dongyang.devmatch.service;

import com.dongyang.devmatch.entity.StudyPost;
import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.repository.StudyPostRepository;
import com.dongyang.devmatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final UserRepository userRepository;

    public List<StudyPost> findAll() {
        return studyPostRepository.findAll();
    }

    public Optional<StudyPost> findById(Long id) {
        return studyPostRepository.findById(id);
    }

    public StudyPost create(StudyPost post, User user) {
        post.setAuthor(user);
        return studyPostRepository.save(post);
    }

    public StudyPost update(StudyPost existingPost, StudyPost updatedData) {
        existingPost.setTitle(updatedData.getTitle());
        existingPost.setContent(updatedData.getContent());
        return studyPostRepository.save(existingPost);
    }

    public void delete(StudyPost post) {
        studyPostRepository.delete(post);
    }

    public List<StudyPost> findByAuthorUsername(String username) {
        return studyPostRepository.findByAuthorUsername(username);
    }

}
