package com.library.library_management.service;

import com.library.library_management.dto.MemberDTO;

import java.util.List;

public interface MemberService {

    MemberDTO createMember(MemberDTO memberDTO);

    MemberDTO getMemberById(Long id);

    List<MemberDTO> getAllMembers();

    MemberDTO updateMember(Long id, MemberDTO memberDTO);

    void deleteMember(Long id);
}