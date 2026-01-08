package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerRepository;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Game Module")
@Feature("GamePlayer Management")
@Owner("DP1-tutors")
@SpringBootTest
class GamePlayerServiceTests {

    @Autowired
    private GamePlayerService gamePlayerService;

    @MockBean
    private GamePlayerRepository gamePlayerRepository;

    private GamePlayer gp;

    @BeforeEach
    void setup() {
        gp = new GamePlayer();
        gp.setId(1);
        gp.setUser(null);
        gp.setGame(null);
        gp.setEnergy(3);
        gp.setColor(Color.RED);
        gp.setCardsPlayedThisRound(0);
    }

    @Test
    void shouldFindGamePlayerById() {
        when(gamePlayerRepository.findById(1)).thenReturn(Optional.of(gp));
        GamePlayer found = gamePlayerService.findById(1);
        assertEquals(gp.getId(), found.getId());
        assertEquals(Color.RED, found.getColor());
    }

    @Test
    void shouldFailFindGamePlayerById_NotFound() {
        when(gamePlayerRepository.findById(25)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gamePlayerService.findById(25));
    }

    @Test
    void shouldUpdatePlayerColor() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20))
                .thenReturn(Optional.of(gp));
        when(gamePlayerRepository.save(gp)).thenReturn(gp);
        GamePlayer result = gamePlayerService.updatePlayerColor(10, 20, "BLUE");
        assertEquals(Color.BLUE, result.getColor());
    }

    @Test
    void shouldFailUpdateColor_GamePlayerNotFound() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20))
                .thenReturn(Optional.empty());
        assertThrows(
            ResourceNotFoundException.class,
            () -> gamePlayerService.updatePlayerColor(10, 20, "GREEN")
        );
    }

    @Test
    void shouldFailUpdateColor_InvalidColor() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20))
                .thenReturn(Optional.of(gp));
        assertThrows(
            IllegalArgumentException.class,
            () -> gamePlayerService.updatePlayerColor(10, 20, "INVALID_COLOR")
        );
    }

    @Test
    void shouldGetById() {
        when(gamePlayerRepository.findById(1)).thenReturn(Optional.of(gp));
        GamePlayer found = gamePlayerService.getById(1);
        assertEquals(1, found.getId());
    }

    @Test
    void shouldFailGetById_NotFound() {
        when(gamePlayerRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gamePlayerService.getById(99));
    }

    @Test
    void shouldFindGamePlayerByGameAndUser() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20))
                .thenReturn(Optional.of(gp));
        GamePlayer found = gamePlayerService.getGamePlayer(10, 20);
        assertEquals(1, found.getId());
    }

    @Test
    void shouldFailFindGamePlayerByGameAndUser_NotFound() {
        when(gamePlayerRepository.findByGameIdAndUserId(10, 20))
                .thenReturn(Optional.empty());
        assertThrows(
            ResourceNotFoundException.class,
            () -> gamePlayerService.getGamePlayer(10, 20)
        );
    }

    @Test
    @Transactional
    void shouldIncrementCardsPlayed() {
        gp.setCardsPlayedThisRound(2);
        when(gamePlayerRepository.findById(1)).thenReturn(Optional.of(gp));
        gamePlayerService.incrementCardsPlayedThisRound(gamePlayerRepository.findById(1).get());
        ArgumentCaptor<GamePlayer> captor = ArgumentCaptor.forClass(GamePlayer.class);
        verify(gamePlayerRepository).save(captor.capture());
        GamePlayer updated = captor.getValue();
        assertEquals(3, updated.getCardsPlayedThisRound());
    }

    /*
    @Test
    void shouldFailIncrementCardsPlayed_NotFound() {
        when(gamePlayerRepository.findById(50)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gamePlayerService.incrementCardsPlayedThisRound(gamePlayerRepository.findById(50).get()));
    }
    */
}

