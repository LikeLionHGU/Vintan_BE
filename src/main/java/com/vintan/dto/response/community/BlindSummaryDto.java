package com.vintan.dto.response.community;

import com.vintan.domain.BlindCommunityPost;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class BlindSummaryDto {
    private Long id;
    private double rate;
    private String title;
    private String date;
    private String userId;

    public BlindSummaryDto (BlindCommunityPost post) {
        this.id = post.getId();
        this.rate = (post.getCategoryRate().getCleanness() + post.getCategoryRate().getRentFee() + post.getCategoryRate().getReach() + post.getCategoryRate().getPeople()) / 4.0;
        this.title = post.getTitle();
        this.date = post.getRegDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.userId = post.getUser().getId();
    }
}
