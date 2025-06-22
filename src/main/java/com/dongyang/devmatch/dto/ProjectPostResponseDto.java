package com.dongyang.devmatch.dto;

import com.dongyang.devmatch.entity.ProjectPost;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ProjectPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String location;
    private Integer recruitCount;
    private String authorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectPostResponseDto toDto(ProjectPost post) {
        return new ProjectPostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLocation(),
                post.getRecruitCount(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
