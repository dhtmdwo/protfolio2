package com.example.appapi.users.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

public class UsersDto {
    @Getter
    public static class SignupRequest {
        @Size(min=4, max=20, message="아이디는 4~20글자까지 입력 가능합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message="아이디는 영어, 숫자만 가능합니다. (대소문자 구분)")
        @NotBlank(message="아이디는 필수 입력값 입니다.")
        @Setter
        @Schema(description = "사용자 아이디", example = "seller")
        private String userId;

        @Size(min=8, message="비밀번호는 8자 이상 입력 해주십시오.")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 영어, 숫자, 특수문자를 모두 포함하여 8자 이상으로 입력 해주십시오."
        )
        @NotBlank(message="비밀번호는 필수 입력값 입니다.")
        @Schema(description = "비밀번호", example = "qwer1234!")
        private String password;

        @Size(min=8, max=8, message="생년월일은 8자로 입력 해주십시오.")
        @NotBlank(message="생년월일은 필수 입력값 입니다.")
        @Schema(description = "생년월일", example = "19700101")
        private String birthDate;

        @Size(min=2, max=17, message="이름은 2~17글자까지 입력 가능합니다.")
        @Pattern(regexp = "^[가-힣]+$", message="이름은 한글만 입력 가능합니다.")
        @Setter
        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;

        @Size(max=254, message="이메일은 254자까지 입력 가능합니다.")
        @Email
        @NotBlank(message="이메일은 필수 입력값 입니다.")
        @Schema(description = "이메일 주소", example = "user@naver.com")
        private String email;

        @NotBlank(message="주소는 필수 입력값 입니다.")
        @Schema(description = "주소", example = "서울특별시 동작구 보라매로87")
        private String address;

        @NotBlank(message="상세주소는 필수 입력값 입니다.")
        @Schema(description = "상세주소", example = "신이빌딩 4층")
        private String addressDetail;

        @Size(max=13, message="전화번호는 13자까지 입력 가능합니다.")
        @Pattern(regexp = "^[0-9]+$", message="전화번호는 숫자만 입력 해주십시오.")
        @NotBlank(message="전화번호는 필수 입력값 입니다.")
        @Schema(description = "전화번호", example = "01012345678")
        private String phone;

        @NotNull(message="회원타입은 필수 입력값 입니다.")
        @Setter
        @Schema(description = "회원타입", example = "SELLER")
        private UserType userType;

        public Users toEntity(String encodedPassword) {
            Users users = Users.builder()
                    .userId(userId)
                    .password(encodedPassword)
                    .birthDate(birthDate)
                    .name(name)
                    .email(email)
                    .address(address)
                    .addressDetail(addressDetail)
                    .phone(phone)
                    .userType(userType)
                    .build();
            return users;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class SignupResponse {
        private Long idx;
        private String userId;
        private UserType userType;
        private String message;

        public static SignupResponse from(Users user) {
            return SignupResponse.builder()
                    .idx(user.getIdx())
                    .userId(user.getUserId())
                    .userType(user.getUserType())
                    .message("회원가입 성공")
                    .build();
        }

        public static SignupResponse error(String message) {
            return SignupResponse.builder()
                    .message(message)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class UserResponse {
        private Long idx;
        private String userId;
        private String email;
        private String address;
        private String addressDetail;
        private String phone;
        private UserType userType;

        public static UserResponse from(Users user) {
            return UserResponse.builder()
                    .idx(user.getIdx())
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .addressDetail(user.getAddressDetail())
                    .phone(user.getPhone())
                    .userType(user.getUserType())
                    .build();
        }

        public static UserResponse error(String message) {
            return UserResponse.builder()
                    .build();
        }
    }

}
