package es.us.dp1.lIng_04_25_26.endofline.board;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

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

import es.us.dp1.lIng_04_25_26.endofline.board.dto.BoardPlaceDTO;
import es.us.dp1.lIng_04_25_26.endofline.board.dto.BoardStateDTO;
import es.us.dp1.lIng_04_25_26.endofline.card.Card;
import es.us.dp1.lIng_04_25_26.endofline.card.CardService;
import es.us.dp1.lIng_04_25_26.endofline.configuration.SecurityConfiguration;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.card.CardForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.game.TurnForbiddenException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

@WebMvcTest(controllers = BoardController.class)
public class BoardControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardService cardService;

    @MockBean
    private GamePlayerService gamePlayerService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    private User user;
    private Game game;
    private GamePlayer gamePlayer;
    private Card card;
    private BoardStateDTO boardStateDTO;

    private static final String BASE_URL = "/api/v1/board";
    private static final Integer GAME_ID = 1;
    private static final Integer USER_ID = 100;
    private static final Integer PLAYER_ID = 50;
    private static final Integer CARD_ID = 10;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setUsername("testuser");

        game = new Game();
        game.setId(GAME_ID);
        game.setStartedAt(LocalDateTime.now());

        gamePlayer = new GamePlayer();
        gamePlayer.setId(PLAYER_ID);
        gamePlayer.setUser(user);
        gamePlayer.setGame(game);

        card = new Card();
        card.setId(CARD_ID);

        boardStateDTO = new BoardStateDTO(
            USER_ID, GAME_ID, PLAYER_ID, LocalDateTime.now(), null, null,
            List.of(), List.of(), 3, 1, 1, null, true, true,
            List.of(), List.of(), List.of(), false
        );
    }


    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testGetStateSuccess() throws Exception {
        when(userService.findCurrentUser()).thenReturn(user);
        when(gameService.getGameById(GAME_ID)).thenReturn(game);
        when(gamePlayerService.isSpectating(game, user)).thenReturn(false);
        when(gamePlayerService.getGamePlayerOrFriend(game, user)).thenReturn(gamePlayer);
        when(boardService.getState(eq(gamePlayer), eq(game), eq(false))).thenReturn(boardStateDTO);

        mockMvc.perform(get(BASE_URL + "/{gameId}/state", GAME_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value(GAME_ID))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.spectating").value(false));
    }


    @Test
    @WithMockUser(username = "spectator", authorities = "PLAYER")
    void testGetStateAsSpectator() throws Exception {
        boardStateDTO.setSpectating(true);

        when(userService.findCurrentUser()).thenReturn(user);
        when(gameService.getGameById(GAME_ID)).thenReturn(game);
        when(gamePlayerService.isSpectating(game, user)).thenReturn(true);
        when(gamePlayerService.getGamePlayerOrFriend(game, user)).thenReturn(gamePlayer);
        when(boardService.getState(eq(gamePlayer), eq(game), eq(true))).thenReturn(boardStateDTO);

        mockMvc.perform(get(BASE_URL + "/{gameId}/state", GAME_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spectating").value(true));
    }


    @Test
    void testGetStateUnauthenticated() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{gameId}/state", GAME_ID))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testGetStateGameNotFound() throws Exception {
        when(gameService.getGameById(99)).thenThrow(new RuntimeException("Game not found"));

        mockMvc.perform(get(BASE_URL + "/{gameId}/state", 99))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testChangeDeckSuccess() throws Exception {
        when(userService.findCurrentUser()).thenReturn(user);
        when(gamePlayerService.getGamePlayer(GAME_ID, USER_ID)).thenReturn(gamePlayer);
        doNothing().when(cardService).changeDeck(gamePlayer);

        mockMvc.perform(post(BASE_URL + "/{gameId}/change", GAME_ID)
                        .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testChangeDeckTurnForbidden() throws Exception {
        when(userService.findCurrentUser()).thenReturn(user);
        when(gamePlayerService.getGamePlayer(GAME_ID, USER_ID)).thenReturn(gamePlayer);
        
        doThrow(new TurnForbiddenException()).when(cardService).changeDeck(gamePlayer);

        mockMvc.perform(post(BASE_URL + "/{gameId}/change", GAME_ID)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testPlaceCardSuccess() throws Exception {
        BoardPlaceDTO placeDTO = new BoardPlaceDTO(CARD_ID, 25);

        when(userService.findCurrentUser()).thenReturn(user);
        when(gamePlayerService.getGamePlayer(GAME_ID, USER_ID)).thenReturn(gamePlayer);
        when(cardService.getCardById(CARD_ID)).thenReturn(card);
        doNothing().when(boardService).placeCard(gamePlayer, card, 25);

        mockMvc.perform(post(BASE_URL + "/{gameId}/place", GAME_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeDTO)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testPlaceCardValidationFailure() throws Exception {
        BoardPlaceDTO invalidDTO = new BoardPlaceDTO(null, null);

        mockMvc.perform(post(BASE_URL + "/{gameId}/place", GAME_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "testuser", authorities = "PLAYER")
    void testPlaceCardForbiddenPosition() throws Exception {
        BoardPlaceDTO placeDTO = new BoardPlaceDTO(CARD_ID, 99);

        when(userService.findCurrentUser()).thenReturn(user);
        when(gamePlayerService.getGamePlayer(GAME_ID, USER_ID)).thenReturn(gamePlayer);
        when(cardService.getCardById(CARD_ID)).thenReturn(card);
        
        doThrow(new CardForbiddenException()).when(boardService).placeCard(any(), any(), eq(99));

        mockMvc.perform(post(BASE_URL + "/{gameId}/place", GAME_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeDTO)))
                .andExpect(status().isForbidden());
    }

}