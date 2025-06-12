export default class LoginResponseDTO {
    constructor(accessToken, refreshToken, user = null) {
        if (!accessToken) {
            throw new Error('액세스 토큰은 필수 항목입니다.');
        }

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
        this.expiresAt = this.calculateExpiresAt();
    }

    calculateExpiresAt() {
        // JWT 토큰의 만료 시간을 계산 (기본값: 1시간)
        const now = new Date();
        return new Date(now.getTime() + 60 * 60 * 1000);
    }

    isExpired() {
        return new Date() > this.expiresAt;
    }

    toJSON() {
        return {
            accessToken: this.accessToken,
            refreshToken: this.refreshToken,
            user: this.user,
            expiresAt: this.expiresAt
        };
    }
}