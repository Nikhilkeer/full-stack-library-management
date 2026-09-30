package com.library.library_management.service;

import com.library.library_management.dto.BorrowRecordDTO;

import java.util.List;

public interface BorrowRecordService {

    BorrowRecordDTO borrowBook(Long bookId, Long memberId);

    BorrowRecordDTO returnBook(Long borrowRecordId);

    List<BorrowRecordDTO> getAllBorrowRecords();

    List<BorrowRecordDTO> getBorrowRecordsByMember(Long memberId);

    List<BorrowRecordDTO> getBorrowRecordsByStatus(String status);
}