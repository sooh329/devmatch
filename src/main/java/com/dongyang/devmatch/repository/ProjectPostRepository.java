package com.dongyang.devmatch.repository;

import com.dongyang.devmatch.entity.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
    List<ProjectPost> findByAuthorUsername(String username);

}
