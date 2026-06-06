package com.ams.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "marks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "assessment_name", nullable = false, length = 50)
    private String assessmentName;

    @Column(name = "marks_obtained", nullable = false)
    private double marksObtained;

    @Column(name = "max_marks", nullable = false)
    private double maxMarks;

    @Column(name = "weightage_percentage", nullable = false)
    private double weightagePercentage;
}
