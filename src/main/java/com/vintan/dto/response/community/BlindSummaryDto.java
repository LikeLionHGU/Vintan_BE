package com.vintan.dto.response.community;

import com.vintan.domain.BlindCommunityPost;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

/**
 * DTO representing a summarized view of a blind community post.
 */
@Getter
public class BlindSummaryDto {

    private Long id;       // Post ID
    private double rate;   // Average rating of the post
    private String title;  // Post title
    private String date;   // Post creation date (formatted yyyy.MM.dd)
    private String userId; // ID of the user who created the post
    private String address;

    /**
     * Constructs a BlindSummaryDto from a BlindCommunityPost entity.
     *
     * @param post the BlindCommunityPost entity
     */
    public BlindSummaryDto(BlindCommunityPost post) {
        this.id = post.getId();
        this.rate = (post.getCategoryRate().getCleanness()
                + post.getCategoryRate().getRentFee()
                + post.getCategoryRate().getReach()
                + post.getCategoryRate().getPeople()) / 4.0; // Average of category ratings
        this.title = post.getTitle();
        this.date = post.getRegDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.userId = post.getUser().getId();
        this.address = post.getAddress();
    }
}
