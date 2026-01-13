package es.us.dp1.lIng_04_25_26.endofline.gameplayer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import es.us.dp1.lIng_04_25_26.endofline.exceptions.gameplayer.GamePlayerNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
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
    @SuppressWarnings("unused")
    private ObjectMapper objectMapper;

    @MockBean
    private GamePlayerService gamePlayerService;

    private GamePlayer gp;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(7);
        user.setUsername("testPlayer");

        gp = new GamePlayer();
        gp.setId(1);
        gp.setEnergy(3);
        gp.setColor(Color.RED);
        gp.setCardsPlayedThisRound(0);
        gp.setUser(user);
    }


    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldUpdatePlayerColorSuccessfully() throws Exception {
        when(gamePlayerService.updatePlayerColor(eq(5), eq(7), any(String.class))).thenAnswer(inv -> {
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
            .andExpect(jsonPath("$.energy").value(3))
            .andExpect(jsonPath("$.user.id").value(7))
            .andExpect(jsonPath("$.user.username").value("testPlayer"));
    }


    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldReturnNotFoundWhenUpdatingColorForNonExistingGamePlayer() throws Exception {
        when(gamePlayerService.updatePlayerColor(anyInt(), anyInt(), any(String.class)))
            .thenThrow(new GamePlayerNotFoundException(20, 10));

        mockMvc.perform(put(BASE + "/{gameId}/{userId}/color", 10, 20)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"GREEN\""))
            .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "playerUser", authorities = { "PLAYER" })
    void shouldReturnBadRequestWhenUpdatingColorWithInvalidValue() throws Exception {
        when(gamePlayerService.updatePlayerColor(anyInt(), anyInt(), any(String.class)))
            .thenThrow(new IllegalArgumentException("No enum constant..."));

        mockMvc.perform(put(BASE + "/{gameId}/{userId}/color", 3, 4)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"INVALID_COLOR\""))
            .andExpect(status().isBadRequest());
    }

}