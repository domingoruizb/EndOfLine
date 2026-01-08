package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lIng_04_25_26.endofline.enums.Color;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerController;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Game Module")
@Feature("GamePlayer Controller")
@Owner("DP1-tutors")
@WebMvcTest(controllers = GamePlayerController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class GamePlayerControllerTests {

    private static final String BASE = "/api/v1/gameplayers";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GamePlayerService gamePlayerService;

    private GamePlayer gp;

    @BeforeEach
    void setUp() {
        gp = new GamePlayer();
        gp.setId(1);
        gp.setEnergy(3);
        gp.setColor(Color.RED);
        gp.setCardsPlayedThisRound(0);
    }

    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldUpdatePlayerColorSuccessfully() throws Exception {
        when(gamePlayerService.updatePlayerColor(5, 7, "BLUE")).thenAnswer(inv -> {
            gp.setColor(Color.BLUE);
            return gp;
        });

        mockMvc.perform(put(BASE + "/{gameId}/{userId}/color", 5, 7)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"BLUE\""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.color").value("BLUE"))
            .andExpect(jsonPath("$.energy").value(3));
    }

    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldReturnNotFoundWhenUpdatingColorForNonExistingGamePlayer() throws Exception {
        when(gamePlayerService.updatePlayerColor(10, 20, "GREEN"))
            .thenThrow(new ResourceNotFoundException("GamePlayer", "GameId/UserId", "10/20"));

        mockMvc.perform(put(BASE + "/{gameId}/{userId}/color", 10, 20)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"GREEN\""))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldReturnBadRequestWhenUpdatingColorWithInvalidValue() throws Exception {
        when(gamePlayerService.updatePlayerColor(3, 4, "INVALID_COLOR"))
            .thenThrow(new IllegalArgumentException("Invalid color"));

        mockMvc.perform(put(BASE + "/{gameId}/{userId}/color", 3, 4)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"INVALID_COLOR\""))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldGetGamePlayerSuccessfully() throws Exception {
        when(gamePlayerService.getGamePlayer(2, 9)).thenReturn(gp);

        mockMvc.perform(get(BASE + "/{gameId}/{userId}", 2, 9))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.color").value("RED"))
            .andExpect(jsonPath("$.energy").value(3));
    }

    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldReturnNotFoundWhenGettingNonExistingGamePlayer() throws Exception {
        when(gamePlayerService.getGamePlayer(11, 22))
            .thenThrow(new ResourceNotFoundException("GamePlayer", "GameId/UserId", "11/22"));

        mockMvc.perform(get(BASE + "/{gameId}/{userId}", 11, 22))
            .andExpect(status().isNotFound());
    }
}
