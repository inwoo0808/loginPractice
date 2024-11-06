package com.kakao_practice.back.service;

import org.springframework.http.ResponseEntity;

import com.kakao_practice.back.dto.request.CheckCertificationRequestDto;
import com.kakao_practice.back.dto.request.IdCheckRequestDto;
import com.kakao_practice.back.dto.request.auth.EmailCertificaionRequestDto;
import com.kakao_practice.back.dto.request.auth.SignInRequestDto;
import com.kakao_practice.back.dto.request.auth.SignUpRequestDto;
import com.kakao_practice.back.dto.response.auth.IdCheckResponseDto;
import com.kakao_practice.back.dto.response.auth.SignInResponseDto;
import com.kakao_practice.back.dto.response.auth.SignUpResponseDto;
import com.kakao_practice.back.dto.response.CheckCertificationResponseDto;
import com.kakao_practice.back.dto.response.auth.EmailCertificationResponseDto;
public interface AuthService {

    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificaionRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);

    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
}
