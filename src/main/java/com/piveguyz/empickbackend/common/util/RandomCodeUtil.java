package com.piveguyz.empickbackend.common.util;

import java.security.SecureRandom;

// 실무테스트 입장 시에 필요한 랜덤 코드 생성 클래스
public class RandomCodeUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 지정된 길이만큼 랜덤 코드 생성 (대문자 + 숫자)
     */
    public static String generateCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
