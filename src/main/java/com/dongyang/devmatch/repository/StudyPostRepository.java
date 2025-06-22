package com.dongyang.devmatch.repository;

import com.dongyang.devmatch.entity.StudyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    List<StudyPost> findByAuthorUsername(String username);
}
