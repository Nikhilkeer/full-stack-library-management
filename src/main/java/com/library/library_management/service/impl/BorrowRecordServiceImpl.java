package com.library.library_management.service.impl;

import com.library.library_management.dto.BorrowRecordDTO;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.BorrowRecord;
import com.library.library_management.entity.BorrowStatus;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BorrowRecordRepository;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.BorrowRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository,
                                   BookRepository bookRepository,
                                   MemberRepository memberRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public BorrowRecordDTO borrowBook(Long bookId, Long memberId) {
        // Step 1: Book exist karta hai ya nahi check karo
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Step 2: Member exist karta hai ya nahi check karo
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        // Step 3: BUSINESS RULE - available copies check karo
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for book: " + book.getTitle());
        }

        // Step 4: Available copies ek kam karo
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Step 5: BorrowRecord create karo
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setBorrowDate(LocalDate.now());
        record.setStatus(BorrowStatus.BORROWED);

        BorrowRecord savedRecord = borrowRecordRepository.save(record);
        return mapToDTO(savedRecord);
    }

    @Override
    @Transactional
    public BorrowRecordDTO returnBook(Long borrowRecordId) {
        // Step 1: BorrowRecord dhoondo
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrow record not found with id: " + borrowRecordId));

        // Step 2: BUSINESS RULE - already returned hai toh dubara return na ho
        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("This book is already returned");
        }

        // Step 3: Status aur return date update karo
        record.setStatus(BorrowStatus.RETURNED);
        record.setReturnDate(LocalDate.now());

        // Step 4: Available copies wapas badhao
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        BorrowRecord updatedRecord = borrowRecordRepository.save(record);
        return mapToDTO(updatedRecord);
    }

    @Override
    public List<BorrowRecordDTO> getAllBorrowRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordDTO> getBorrowRecordsByMember(Long memberId) {
        return borrowRecordRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordDTO> getBorrowRecordsByStatus(String status) {
        BorrowStatus statusEnum = BorrowStatus.valueOf(status.toUpperCase());
        return borrowRecordRepository.findByStatus(statusEnum)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BorrowRecordDTO mapToDTO(BorrowRecord record) {
        BorrowRecordDTO dto = new BorrowRecordDTO();
        dto.setId(record.getId());
        dto.setBookId(record.getBook().getId());
        dto.setBookTitle(record.getBook().getTitle());
        dto.setMemberId(record.getMember().getId());
        dto.setMemberName(record.getMember().getName());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus());
        return dto;
    }
}