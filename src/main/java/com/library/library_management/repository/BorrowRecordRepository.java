package com.library.library_management.repository;

import com.library.library_management.entity.BorrowRecord;
import com.library.library_management.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByMemberId(Long memberId);

    List<BorrowRecord> findByBookId(Long bookId);

    List<BorrowRecord> findByStatus(BorrowStatus status);

    List<BorrowRecord> findByMemberIdAndStatus(Long memberId, BorrowStatus status);
}