package com.library.library_management.service.impl;

import com.library.library_management.dto.MemberDTO;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.MemberRepository;
import com.library.library_management.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public MemberDTO createMember(MemberDTO memberDTO) {
        // Business rule: duplicate email allow nahi karenge
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new IllegalArgumentException("Member already exists with email: " + memberDTO.getEmail());
        }

        Member member = mapToEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return mapToDTO(savedMember);
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return mapToDTO(member);
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        existingMember.setName(memberDTO.getName());
        existingMember.setPhoneNumber(memberDTO.getPhoneNumber());
        existingMember.setAddress(memberDTO.getAddress());
        // Email intentionally update nahi kar rahe - ek business decision

        Member updatedMember = memberRepository.save(existingMember);
        return mapToDTO(updatedMember);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        memberRepository.delete(member);
    }

    private MemberDTO mapToDTO(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getAddress()
        );
    }

    private Member mapToEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setAddress(memberDTO.getAddress());
        return member;
    }
}