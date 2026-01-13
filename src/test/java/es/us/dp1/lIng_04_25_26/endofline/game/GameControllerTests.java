package es.us.dp1.lIng_04_25_26.endofline.game;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lIng_04_25_26.endofline.enums.Skill;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.game.GameBadRequestException;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.game.GameNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayer;
import es.us.dp1.lIng_04_25_26.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class GameControllerTests {

    private static final String BASE_URL = "/api/v1/games";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private GamePlayerService gamePlayerService;

    private User host;
    private User player2;
    private Game game;
    private GamePlayer gamePlayerHost;

    @BeforeEach
    void setUp() {
        host = new User();
        host.setId(1);
        host.setUsername("hostUser");
        host.setEmail("host@test.com");

        player2 = new User();
        player2.setId(2);
        player2.setUsername("guestUser");
        player2.setEmail("guest@test.com");

        game = new Game();
        game.setId(100);
        game.setCode("ABC1234");
        game.setRound(0);
        game.setHost(host);
        game.setWinner(null);
        game.setStartedAt(null);
        game.setEndedAt(null);
        game.setGamePlayers(new ArrayList<>());
        game.setTurn(host.getId());

        gamePlayerHost = new GamePlayer();
        gamePlayerHost.setId(10);
        gamePlayerHost.setUser(host);
        gamePlayerHost.setGame(game);
        
        game.getGamePlayers().add(gamePlayerHost);
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testFindAllGames() throws Exception {
        when(gameService.findAll()).thenReturn(List.of(game));

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(game.getId()))
            .andExpect(jsonPath("$[0].code").value(game.getCode()))
            .andExpect(jsonPath("$[0].host.username").value(host.getUsername()));
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testGetGameById() throws Exception {
        when(gameService.getGameById(100)).thenReturn(game);

        mockMvc.perform(get(BASE_URL + "/{id}", 100))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()))
            .andExpect(jsonPath("$.round").value(0))
            .andExpect(jsonPath("$.host.id").value(host.getId()));
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testGetGameByIdNotFound() throws Exception {
        when(gameService.getGameById(999)).thenThrow(new GameNotFoundException(999));

        mockMvc.perform(get(BASE_URL + "/{id}", 999))
            .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testCreateGame() throws Exception {
        when(userService.findCurrentUser()).thenReturn(host);
        when(gameService.createGame(host)).thenReturn(game);

        mockMvc.perform(post(BASE_URL + "/create")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()))
            .andExpect(jsonPath("$.host.username").value(host.getUsername()));
    }


    @Test
    @WithMockUser(username = "guestUser", authorities = {"PLAYER"})
    void testJoinGame() throws Exception {
        String code = "ABC1234";
        
        Game joinedGame = new Game();
        joinedGame.setId(100);
        joinedGame.setCode(code);
        joinedGame.setHost(host);
        joinedGame.setGamePlayers(new ArrayList<>());
        joinedGame.getGamePlayers().add(gamePlayerHost);
        
        GamePlayer gamePlayerGuest = new GamePlayer();
        gamePlayerGuest.setUser(player2);
        joinedGame.getGamePlayers().add(gamePlayerGuest);

        when(userService.findCurrentUser()).thenReturn(player2);
        when(gameService.joinGameByCode(player2, code)).thenReturn(joinedGame);

        mockMvc.perform(post(BASE_URL + "/join/{code}", code)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(joinedGame.getId()))
            .andExpect(jsonPath("$.gamePlayers", hasSize(2)));
    }


    @Test
    @WithMockUser(username = "guestUser", authorities = {"PLAYER"})
    void testJoinGameNotFound() throws Exception {
        String invalidCode = "INVALID";
        when(userService.findCurrentUser()).thenReturn(player2);
        when(gameService.joinGameByCode(player2, invalidCode)).thenThrow(new GameNotFoundException(invalidCode));

        mockMvc.perform(post(BASE_URL + "/join/{code}", invalidCode)
                .with(csrf()))
            .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testStartGame() throws Exception {
        game.setStartedAt(LocalDateTime.now());
        game.setRound(1);

        when(gameService.getGameById(100)).thenReturn(game);
        when(gameService.startGame(game)).thenReturn(game);

        mockMvc.perform(post(BASE_URL + "/{id}/start", 100)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()))
            .andExpect(jsonPath("$.round").value(1))
            .andExpect(jsonPath("$.startedAt").exists());
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testStartGameBadRequest() throws Exception {
        when(gameService.getGameById(100)).thenReturn(game);
        when(gameService.startGame(game)).thenThrow(new GameBadRequestException("Game needs to have 2 players"));

        mockMvc.perform(post(BASE_URL + "/{id}/start", 100)
                .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists());
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testGiveUp() throws Exception {
        game.setWinner(player2);
        game.setEndedAt(LocalDateTime.now());

        when(userService.findCurrentUser()).thenReturn(host);
        when(gamePlayerService.getGamePlayer(100, host.getId())).thenReturn(gamePlayerHost);
        when(gameService.giveUpOrLose(gamePlayerHost)).thenReturn(game);

        mockMvc.perform(post(BASE_URL + "/{gameId}/giveup", 100)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.winner.username").value(player2.getUsername()));
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testSetUpSkill() throws Exception {
        SkillRequestDTO skillRequest = new SkillRequestDTO();
        skillRequest.setSkill("SPEED_UP");

        game.setSkill(Skill.SPEED_UP);

        when(userService.findCurrentUser()).thenReturn(host);
        when(gamePlayerService.getGamePlayer(100, host.getId())).thenReturn(gamePlayerHost);
        when(gameService.setUpSkill(any(GamePlayer.class), eq(Skill.SPEED_UP))).thenReturn(game);

        mockMvc.perform(post(BASE_URL + "/{gameId}/skill", 100)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.skill", is("SPEED_UP")));
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testSetUpSkillInvalidEnum() throws Exception {
        String invalidJson = "{ \"skill\": \"SUPER_POWER_NOT_EXIST\" }";

        mockMvc.perform(post(BASE_URL + "/{gameId}/skill", 100)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "hostUser", authorities = {"PLAYER"})
    void testDeleteGame() throws Exception {
        when(gameService.getGameById(100)).thenReturn(game);
        doNothing().when(gameService).deleteGame(game);

        mockMvc.perform(delete(BASE_URL + "/{gameId}", 100)
                .with(csrf()))
            .andExpect(status().isOk());
        
        verify(gameService).deleteGame(game);
    }


    @Test
    @WithMockUser(username = "guestUser", authorities = {"PLAYER"})
    void testDeleteGameNotAllowed() throws Exception {
        when(gameService.getGameById(100)).thenReturn(game);
        doThrow(new GameBadRequestException("Only the host can delete the game"))
            .when(gameService).deleteGame(game);

        mockMvc.perform(delete(BASE_URL + "/{gameId}", 100)
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

}