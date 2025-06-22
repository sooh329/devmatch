package com.dongyang.devmatch.dto;

import com.dongyang.devmatch.entity.ProjectPost;
import com.dongyang.devmatch.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectPostRequestDto {
    private String title;
    private String content;
    private String location;
    private Integer recruitCount;

    public ProjectPost toEntity(User author) {
        ProjectPost post = new ProjectPost();
        post.setTitle(title);
        post.setContent(content);
        post.setLocation(location);
        post.setRecruitCount(recruitCount);
        post.setAuthor(author);
        return post;
    }
}
