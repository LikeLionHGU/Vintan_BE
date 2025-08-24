package com.vintan.repository;

import com.vintan.domain.BlindCommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing BlindCommunityPost entities.
 * Provides methods for querying posts by region or by user.
 */
@Repository
public interface BlindCommunityPostRepository extends JpaRepository<BlindCommunityPost, Long> {

    /**
     * Find all blind community posts in the specified region.
     *
     * @param regionNo region identifier
     * @return list of BlindCommunityPost
     */
    List<BlindCommunityPost> findByRegionNo(Long regionNo);

    /**
     * Find a blind community post created by the specified user.
     *
     * @param userId user identifier
     * @return BlindCommunityPost or null if not found
     */
    BlindCommunityPost findByUser_Id(String userId);
}
