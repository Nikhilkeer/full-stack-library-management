package com.library.library_management.controller;

import com.library.library_management.dto.BorrowRecordDTO;
import com.library.library_management.service.BorrowRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    public BorrowRecordController(BorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
    }

    // Book borrow karne ke liye - bookId aur memberId query params se lenge
    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecordDTO> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long memberId) {
        BorrowRecordDTO record = borrowRecordService.borrowBook(bookId, memberId);
        return ResponseEntity.ok(record);
    }

    // Book return karne ke liye
    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowRecordDTO> returnBook(@PathVariable Long id) {
        BorrowRecordDTO record = borrowRecordService.returnBook(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping
    public ResponseEntity<List<BorrowRecordDTO>> getAllRecords() {
        return ResponseEntity.ok(borrowRecordService.getAllBorrowRecords());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecordDTO>> getRecordsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowRecordService.getBorrowRecordsByMember(memberId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowRecordDTO>> getRecordsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(borrowRecordService.getBorrowRecordsByStatus(status));
    }
}