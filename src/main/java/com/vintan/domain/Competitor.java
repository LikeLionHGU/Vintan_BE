package com.vintan.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a competitor business for a report.
 * Stores basic info such as name, address, description, and associated report.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    private String name; // Competitor's business name

    private String address; // Competitor's location/address

    @Lob
    private String description; // Detailed description of the competitor (large text)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report; // Associated report entity (many competitors can belong to one report)
}
