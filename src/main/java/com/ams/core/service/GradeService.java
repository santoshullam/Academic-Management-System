package com.ams.core.service;

import com.ams.core.dto.MarkRecordRequest;
import com.ams.core.dto.MarkRecordResponse;
import com.ams.core.dto.ReportCardDto;

public interface GradeService {
    MarkRecordResponse recordStudentMark(MarkRecordRequest request, String facultyUsername);
    ReportCardDto getStudentReportCard(String studentUsername);
}
