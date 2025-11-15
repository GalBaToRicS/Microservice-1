package ru.itmentor.spring.boot_security.demo.scheduler;

import ru.itmentor.spring.boot_security.demo.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Шедуллер, который отвечает за удаление истекших токенов из черного списка.
 * <p>
 * Этот компонент периодически вызывает метод для очистки токенов, которые
 * достигли момента истечения срока действия, с заданным временным интервалом.
 *
 * <p>Метод {@link #cleanupExpiredTokens()} будет вызываться каждую секунду
 * для удаления токенов, которые истекли.</p>
 */
@Component
@AllArgsConstructor
public class TokenCleanupScheduler {

    private final TokenRepository tokenRepository;

    /**
     * Удаляет токены из черного списка, которые истекли.
     * Этот метод будет вызываться каждую секунду.
     */
    @Scheduled(fixedRate = 36000000)
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteAllByExpBefore(LocalDateTime.now());
    }
}