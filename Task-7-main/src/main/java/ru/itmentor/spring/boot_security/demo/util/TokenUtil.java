package ru.itmentor.spring.boot_security.demo.util;

import ru.itmentor.spring.boot_security.demo.exception.ParseException;
import ru.itmentor.spring.boot_security.demo.model.entity.TokenPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.PARSE_EXCEPTION;

@Component
public class TokenUtil {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Получает имя пользователя из токена.
     *
     * @param token строка, представляющая JWT токен
     * @return имя пользователя из токена
     * @throws ParseException при возникновении ошибки при разборе JSON
     */
    public String getUsername(String token) {
        try {
            String[] chinks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chinks[1]));
            TokenPayload tokenPayload = objectMapper.readValue(payload, TokenPayload.class);
            return tokenPayload.getUsername();
        } catch (Exception e) {
            throw new ParseException(PARSE_EXCEPTION);
        }
    }

    public Long getExt(String token) {
        try {
            String[] chinks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chinks[1]));
            TokenPayload tokenPayload = objectMapper.readValue(payload, TokenPayload.class);
            return tokenPayload.getExp();
        } catch (Exception e) {
            throw new ParseException(PARSE_EXCEPTION);
        }
    }
}