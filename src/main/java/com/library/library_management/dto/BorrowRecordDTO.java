package com.library.library_management.dto;

import com.library.library_management.entity.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordDTO {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long memberId;
    private String memberName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private BorrowStatus status;
}