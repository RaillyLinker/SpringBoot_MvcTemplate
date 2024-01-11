package com.raillylinker.springboot_mvc_template.custom_objects

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.json.BasicJsonParser
import java.nio.charset.StandardCharsets
import java.util.*

// [JWT 토큰 유틸]
object JwtTokenUtilObject {
    // <공개 메소드 공간>
    // (액세스 토큰 발행)
    // memberRoleList : 멤버 권한 리스트 (ex : ["ROLE_ADMIN", "ROLE_DEVELOPER"])
    fun generateAccessToken(
        memberUid: String,
        memberRoleList: List<String>,
        accessTokenExpirationTimeMs: Long,
        jwtClaimsAes256InitializationVector: String,
        jwtClaimsAes256EncryptionKey: String,
        issuer: String,
        jwtSecretKeyString: String
    ): String {
        return doGenerateToken(
            memberUid,
            memberRoleList,
            "access",
            accessTokenExpirationTimeMs,
            jwtClaimsAes256InitializationVector,
            jwtClaimsAes256EncryptionKey,
            issuer,
            jwtSecretKeyString
        )
    }

    // (리프레시 토큰 발행)
    fun generateRefreshToken(
        memberUid: String,
        refreshTokenExpirationTimeMs: Long,
        jwtClaimsAes256InitializationVector: String,
        jwtClaimsAes256EncryptionKey: String,
        issuer: String,
        jwtSecretKeyString: String
    ): String {
        return doGenerateToken(
            memberUid, null, "refresh", refreshTokenExpirationTimeMs,
            jwtClaimsAes256InitializationVector,
            jwtClaimsAes256EncryptionKey,
            issuer,
            jwtSecretKeyString
        )
    }

    // (JWT Secret 확인)
    // : 토큰 유효성 검증. 유효시 true, 위변조시 false
    fun validateSignature(
        token: String,
        jwtSecretKeyString: String
    ): Boolean {
        val tokenSplit = token.split(".")
        val header = tokenSplit[0]
        val payload = tokenSplit[1]
        val signature = tokenSplit[2]

        // base64 로 인코딩된 header 와 payload 를 . 로 묶은 후 이를 시크릿으로 HmacSha256 해싱을 적용하여 signature 를 생성
        val newSig = CryptoUtilObject.hmacSha256("$header.$payload", jwtSecretKeyString)

        // 위 방식으로 생성된 signature 가 token 으로 전달된 signature 와 동일하다면 위/변조되지 않은 토큰으로 판단 가능
        // = 발행시 사용한 시크릿과 검증시 사용된 시크릿이 동일
        return signature == newSig
    }

    // (JWT 정보 반환)
    // Member Uid
    fun getMemberUid(
        token: String,
        jwtClaimsAes256InitializationVector: String,
        jwtClaimsAes256EncryptionKey: String
    ): String {
        return CryptoUtilObject.decryptAES256(
            parseJwtForPayload(token)["mu"].toString(),
            "AES/CBC/PKCS5Padding",
            jwtClaimsAes256InitializationVector,
            jwtClaimsAes256EncryptionKey
        )
    }

    // Token 용도 (access or refresh)
    fun getTokenUsage(
        token: String,
        jwtClaimsAes256InitializationVector: String,
        jwtClaimsAes256EncryptionKey: String
    ): String {
        return CryptoUtilObject.decryptAES256(
            parseJwtForPayload(token)["tu"].toString(),
            "AES/CBC/PKCS5Padding",
            jwtClaimsAes256InitializationVector,
            jwtClaimsAes256EncryptionKey
        )
    }

    // 멤버 권한 리스트 (ex : ["ROLE_ADMIN", "ROLE_DEVELOPER"])
    fun getMemberRoleList(
        token: String,
        jwtClaimsAes256InitializationVector: String,
        jwtClaimsAes256EncryptionKey: String
    ): List<String> {
        return Gson().fromJson(
            CryptoUtilObject.decryptAES256(
                parseJwtForPayload(token)["mrl"].toString(),
                "AES/CBC/PKCS5Padding",
                jwtClaimsAes256InitializationVector,
                jwtClaimsAes256EncryptionKey
            ), // 해석하려는 json 형식의 String
            object : TypeToken<List<String>>() {}.type // 파싱할 데이터 객체 타입
        )
    }

    // 발행자
    fun getIssuer(token: String): String {
        return parseJwtForPayload(token)["iss"].toString()
    }

    // 토큰 남은 유효 시간(초) 반환 (만료된 토큰이라면 0)
    fun getRemainSeconds(token: String): Long {
        val exp = parseJwtForPayload(token)["exp"] as Long

        val diff = exp - (System.currentTimeMillis() / 1000)

        val remain = if (diff <= 0) {
            0
        } else {
            diff
        }

        return remain
    }

    // 토큰 타입
    fun getTokenType(token: String): String {
        return parseJwtForHeader(token)["typ"].toString()
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    // (JWT 토큰 생성)
    private fun doGenerateToken(
        memberUid: String,
        memberRoleList: List<String>?,
        tokenUsage: String,
        expireTimeMs: Long,
        jwtClaimsAes256InitializationVector: String,
        jwtClaimsAes256EncryptionKey: String,
        issuer: String,
        jwtSecretKeyString: String
    ): String {
        val jwtBuilder = Jwts.builder()

        val headersMap = mutableMapOf<String, Any>()

        headersMap["typ"] = "JWT"

        jwtBuilder.header().empty().add(headersMap)

        val claimsMap = mutableMapOf<String, Any>()

        // member uid
        claimsMap["mu"] = CryptoUtilObject.encryptAES256(
            memberUid,
            "AES/CBC/PKCS5Padding",
            jwtClaimsAes256InitializationVector,
            jwtClaimsAes256EncryptionKey
        )

        // token usage
        claimsMap["tu"] = CryptoUtilObject.encryptAES256(
            tokenUsage,
            "AES/CBC/PKCS5Padding",
            jwtClaimsAes256InitializationVector,
            jwtClaimsAes256EncryptionKey
        )

        claimsMap["iss"] = issuer
        claimsMap["iat"] = Date(System.currentTimeMillis())
        claimsMap["exp"] = Date(System.currentTimeMillis() + expireTimeMs)

        if (memberRoleList != null) {
            // member role list
            claimsMap["mrl"] = CryptoUtilObject.encryptAES256(
                Gson().toJson(
                    memberRoleList
                ),
                "AES/CBC/PKCS5Padding",
                jwtClaimsAes256InitializationVector,
                jwtClaimsAes256EncryptionKey
            )
        }

        jwtBuilder.claims().empty().add(claimsMap)

        jwtBuilder
            .signWith(
                Keys.hmacShaKeyFor(jwtSecretKeyString.toByteArray(StandardCharsets.UTF_8)),
                Jwts.SIG.HS256
            )

        return jwtBuilder.compact()
    }

    // (base64 로 인코딩된 Header, Payload 를 base64 로 디코딩)
    private fun parseJwtForHeader(jwt: String): Map<String, Any> {
        val header = CryptoUtilObject.base64Decode(jwt.split(".")[0])
        return BasicJsonParser().parseMap(header)
    }

    private fun parseJwtForPayload(jwt: String): Map<String, Any> {
        val payload = CryptoUtilObject.base64Decode(jwt.split(".")[1])
        return BasicJsonParser().parseMap(payload)
    }
}