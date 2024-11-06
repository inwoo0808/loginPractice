package com.kakao_practice.back.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kakao_practice.back.common.CertificationNumber;
import com.kakao_practice.back.dto.request.CheckCertificationRequestDto;
import com.kakao_practice.back.dto.request.IdCheckRequestDto;
import com.kakao_practice.back.dto.request.auth.EmailCertificaionRequestDto;
import com.kakao_practice.back.dto.request.auth.SignInRequestDto;
import com.kakao_practice.back.dto.request.auth.SignUpRequestDto;
import com.kakao_practice.back.dto.response.CheckCertificationResponseDto;
import com.kakao_practice.back.dto.response.ResponseDto;
import com.kakao_practice.back.dto.response.auth.EmailCertificationResponseDto;
import com.kakao_practice.back.dto.response.auth.IdCheckResponseDto;
import com.kakao_practice.back.dto.response.auth.SignInResponseDto;
import com.kakao_practice.back.dto.response.auth.SignUpResponseDto;
import com.kakao_practice.back.entity.CertificationEntity;
import com.kakao_practice.back.entity.UserEntity;
import com.kakao_practice.back.provider.EmailProvider;
import com.kakao_practice.back.provider.JwtProvider;
import com.kakao_practice.back.repository.CertificationRepository;
import com.kakao_practice.back.repository.UserRepository;
import com.kakao_practice.back.service.AuthService;


import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService{

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
        public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
            try {
                String userId = dto.getId();
                boolean isExistId = userRepository.existsByUserId(userId);
                if(isExistId) return IdCheckResponseDto.duplicateId();
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseDto.databaseError();
            }
    
            return IdCheckResponseDto.success();
        }

    @Override
    public ResponseEntity<? super com.kakao_practice.back.dto.response.auth.EmailCertificationResponseDto> emailCertification(
            EmailCertificaionRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();

            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return EmailCertificationResponseDto.duplicateId();
        
            String certificationNumber = CertificationNumber.getCertificationNumber();
            boolean isSuccessed = emailProvider.sendCerfiticationMail(email, certificationNumber);
            if(!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(certificationEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super com.kakao_practice.back.dto.response.CheckCertificationResponseDto> checkCertification(
            CheckCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if(certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            
            if(!isMatched) return CheckCertificationResponseDto.certificationFail();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsById(userId);
            if(isExistId) return SignUpResponseDto.duplicateId();

            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            System.out.println(email);
            System.out.println(certificationNumber);

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);

            if(!isMatched) return SignUpResponseDto.certificationFail();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);

            dto.setPassword(encodedPassword);
            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            certificationRepository.deleteByUserId(userId);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        String token = null;
        try {
            String userId = dto.getId();
            UserEntity userEntity = userRepository.findByUserId(userId);

            if(userEntity == null) SignInResponseDto.signInFail();

            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if(!isMatched) return SignInResponseDto.signInFail();

            token = jwtProvider.create(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignInResponseDto.success(token);
    }

    
}
