package es.us.dp1.lx_xy_24_25.endofline.game;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.us.dp1.lx_xy_24_25.endofline.board.BoardService;
import es.us.dp1.lx_xy_24_25.endofline.board.BoardUtils;
import es.us.dp1.lx_xy_24_25.endofline.card.Card;
import es.us.dp1.lx_xy_24_25.endofline.card.CardService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardDTO;
import es.us.dp1.lx_xy_24_25.endofline.user.Authorities;
import es.us.dp1.lx_xy_24_25.endofline.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class GameControllerTests {
    private static final String BASE_URL = "/api/v1/games";

    private User player;
    private User player2;
    private GamePlayer gamePlayer1;
    private GamePlayer gamePlayer2;
    private List<GamePlayer> gamePlayers;
    private Authorities authority;
    private Game game;
    private Game game2;

    @SuppressWarnings("unused")
    @Autowired
    private GameController gameController;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private GamePlayerService gamePlayerService;

    @MockBean
    private CardService cardService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        LocalDate birthDate = LocalDate.of(2005, 3, 15);
        String avatar = "https://cdn-icons-png.flaticon.com/512/147/147144.png";
        authority = new Authorities();
        authority.setId(2);
        authority.setAuthority("PLAYER");

        player = new User();
        player.setId(1);
        player.setName("playerName");
        player.setSurname("playerSurname");
        player.setPassword("Play3r!");
        player.setEmail("player@gmail.com");
        player.setBirthdate(birthDate);
        player.setAuthority(authority);
        player.setAvatar(avatar);

        player2 = new User();
        player2.setId(2);
        player2.setName("playerName2");
        player2.setSurname("playerSurname2");
        player2.setPassword("Play3r2!");
        player2.setEmail("player2@gmail.com");
        player2.setBirthdate(birthDate);
        player2.setAuthority(authority);
        player2.setAvatar(avatar);

        when(userService.findUser(1)).thenReturn(player);
        when(userService.findUser(2)).thenReturn(player2);

        gamePlayer1 = new GamePlayer();
        gamePlayer1.setId(1);
        gamePlayer1.setEnergy(3);
        gamePlayer1.setUser(player);

        gamePlayer2 = new GamePlayer();
        gamePlayer2.setId(2);
        gamePlayer2.setEnergy(3);
        gamePlayer2.setUser(player2);

        gamePlayers = List.of(gamePlayer1, gamePlayer2);

        game = new Game();
        game.setId(1);
        game.setRound(1);
        game.setWinner(null);
        game.setStartedAt(LocalDateTime.now());
        game.setEndedAt(null);
        game.setGamePlayers(gamePlayers);
        game.setHost(player);

        game2 = new Game();
        game2.setId(2);
        game2.setRound(10);
        game2.setWinner(player2);
        game2.setStartedAt(LocalDateTime.now().minusDays(1));
        game2.setEndedAt(LocalDateTime.now());
        game2.setGamePlayers(gamePlayers);
        game2.setHost(player);
    }

    @Test
    @WithMockUser("player")
    void findAllGamesTest() throws Exception {
        when(this.gameService.findAll()).thenReturn(List.of(game, game2));
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].round").exists())
            .andExpect(jsonPath("$[0].gamePlayers").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].winner").exists());
    }

    @Test
    @WithMockUser("player")
    void findByIdTest() throws Exception {
        when(this.gameService.getGameById(1)).thenReturn(this.game);
        mockMvc.perform(get(BASE_URL + "/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()))
            .andExpect(jsonPath("$.round").value(game.getRound()))
            .andExpect(jsonPath("$.gamePlayers", hasSize(game.getGamePlayers().size())));
    }

    @Test
    @WithMockUser("player")
    void createGameTest() throws Exception {
        when(this.gameService.createGame(1)).thenReturn(game);
        mockMvc.perform(post(BASE_URL + "/create/{hostId}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()));
    }

    @Test
    @WithMockUser("player")
    void joinGameTest() throws Exception {
        String code = "ABC123";
        when(this.gameService.joinGameByCode(2, code)).thenReturn(game);
        mockMvc.perform(post(BASE_URL + "/join/{userId}/{code}", 2, code))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()));
    }

    @Test
    @WithMockUser("player")
    void startGameTest() throws Exception {
        when(this.gameService.startGame(1)).thenReturn(game);
        mockMvc.perform(post(BASE_URL + "/{id}/start", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()));
    }

    @Test
    @WithMockUser("player")
    void nextTurnTest() throws Exception {
        when(this.gameService.getGameById(1)).thenReturn(game);
        when(this.gameService.getGameById(1)).thenReturn(game);
        mockMvc.perform(post(BASE_URL + "/{id}/next-turn", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()));
        verify(gameService, atLeastOnce()).advanceTurn(any(Game.class));
    }

    @Test
    @WithMockUser("player")
    void endGameTest() throws Exception {
        when(this.gameService.endGame(1, 2)).thenReturn(game2);
        mockMvc.perform(post(BASE_URL + "/{id}/end/{winnerId}", 1, 2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game2.getId()))
            .andExpect(jsonPath("$.winner").exists());
    }

    @Test
    @WithMockUser("player")
    void giveUpTest() throws Exception {
        when(this.gameService.giveUpOrLose(1, 2)).thenReturn(game2);
        mockMvc.perform(put(BASE_URL + "/{gameId}/{userId}/giveup", 1, 2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game2.getId()));
    }

    @Test
    @WithMockUser("player")
    void loseTest() throws Exception {
        when(this.gameService.giveUpOrLose(1, 2)).thenReturn(game2);
        mockMvc.perform(put(BASE_URL + "/{gameId}/{userId}/lose", 1, 2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game2.getId()));
    }

    @Test
    @WithMockUser("player")
    void setUpSkillTest() throws Exception {
        String json = """
            {
                "skill": "SPEED_UP"
            }
            """;
        when(this.gameService.setUpSkill(1, 1, "SPEED_UP")).thenReturn(game);
        mockMvc.perform(put(BASE_URL + "/{gameId}/{userId}/setUpSkill", 1, 1)
                .contentType("application/json")
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(game.getId()));
    }

    @Test
    @WithMockUser("player")
    void deleteGameTest() throws Exception {
        doNothing().when(this.gameService).deleteGame(1);
        mockMvc.perform(delete(BASE_URL + "/{id}", 1))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("player")
    void getPlaceablePositionsTest() throws Exception {
        Integer gamePlayerId = 1;
        GamePlayer gp = new GamePlayer();
        gp.setId(gamePlayerId);
        gp.setUser(player);
        Game minimalGame = new Game();
        minimalGame.setId(5);
        gp.setGame(minimalGame);
        GamePlayerCardDTO dto = new GamePlayerCardDTO();
        dto.setImage("some.png");
        dto.setPositionX(1);
        dto.setPositionY(1);
        dto.setRotation(0);
        dto.setTurnFinished(false);
        Card dummyCard = new Card();
        dummyCard.setId(11);
        dummyCard.setImage("some.png");

        when(this.gamePlayerService.getById(gamePlayerId)).thenReturn(gp);
        when(this.cardService.findByImage("some.png")).thenReturn(dummyCard);
        when(this.boardService.getBoard(minimalGame.getId())).thenReturn(List.of());
        try (MockedStatic<BoardUtils> mockedUtils = mockStatic(BoardUtils.class)) {
            mockedUtils.when(() -> BoardUtils.getValidIndexes(any(), any()))
                    .thenReturn(List.of(1, 2, 3));
            mockMvc.perform(post(BASE_URL + "/{gamePlayerId}/placeable", gamePlayerId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3));
        }
    }

}
