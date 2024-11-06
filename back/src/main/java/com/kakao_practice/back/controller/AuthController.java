package com.kakao_practice.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao_practice.back.dto.request.CheckCertificationRequestDto;
import com.kakao_practice.back.dto.request.IdCheckRequestDto;
import com.kakao_practice.back.dto.request.auth.EmailCertificaionRequestDto;
import com.kakao_practice.back.dto.request.auth.SignInRequestDto;
import com.kakao_practice.back.dto.request.auth.SignUpRequestDto;
import com.kakao_practice.back.dto.response.CheckCertificationResponseDto;
import com.kakao_practice.back.dto.response.auth.EmailCertificationResponseDto;
import com.kakao_practice.back.dto.response.auth.IdCheckResponseDto;
import com.kakao_practice.back.dto.response.auth.SignInResponseDto;
import com.kakao_practice.back.dto.response.auth.SignUpResponseDto;
import com.kakao_practice.back.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(
        @RequestBody @Valid IdCheckRequestDto requestBody
    ){
        ResponseEntity<? super IdCheckResponseDto>response = authService.idCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
        @RequestBody @Valid EmailCertificaionRequestDto requestBody
        ) {
       ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
       return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
        @RequestBody @Valid CheckCertificationRequestDto requestBody
    ){
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(
        @RequestBody @Valid SignUpRequestDto requestBody
    ){
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }
    
    @PostMapping("/sign-in")
    public ResponseEntity<?super SignInResponseDto> signIn(
        @RequestBody @Valid SignInRequestDto requestBody
    ){
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;

    }
    
}
