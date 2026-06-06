package com.ams.core.dto;

public record MarkRecordResponse(
    Long markId,
    double weightedScore,
    String assessmentName
) {}
