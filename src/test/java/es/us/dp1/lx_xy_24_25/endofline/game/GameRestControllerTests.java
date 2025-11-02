package es.us.dp1.lx_xy_24_25.endofline.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.smartcardio.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import net.bytebuddy.asm.Advice.Exit;
import es.us.dp1.lx_xy_24_25.endofline.enums.CardStatus;
import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.enums.Orientation;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.user.Authorities;
import es.us.dp1.lx_xy_24_25.endofline.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserService;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class GameRestControllerTests {
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
    private GameRestController gameRestController;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Game createGame(Integer id, Integer id2, Color color, Color color2) {
        Game game = new Game();
        game.setId(id);
        game.setRound(1);
        game.setWinner(null);
        game.setStartedAt(java.time.LocalDateTime.now());
        game.setEndedAt(null);
        game.setGamePlayers(null);
        return game;
    }



    @BeforeEach
    void setUp() { 
        LocalDate birthDate = LocalDate.of(2005, 3, 15);
        String avatar = "https://cdn-icons-png.flaticon.com/512/147/147144.png";
        authority = new Authorities();
        authority.setId(2);
        authority.setAuthority("PLAYER");


        User player = new User();
        player.setId(1);
        player.setName("playerName");
        player.setSurname("playerSurname");
        player.setPassword("Play3r!");
        player.setEmail("player@gmail.com");
        player.setBirthdate(birthDate);
        player.setAuthority(authority);
        player.setAvatar(avatar);

        when(userService.findUser(1)).thenReturn(player);

        gamePlayer1 = new GamePlayer();
        gamePlayer1.setId(1);
        gamePlayer1.setEnergy(3);
        gamePlayer1.setUser(player);

        User player2 = new User();
        player2.setId(2);
        player2.setName("playerName2");
        player2.setSurname("playerSurname2");
        player2.setPassword("Play3r2!");
        player2.setEmail("player2@gmail.com");
        player2.setBirthdate(birthDate);
        player2.setAuthority(authority);
        player2.setAvatar(avatar);
        
        gamePlayer2 = new GamePlayer();
        gamePlayer2.setId(2);
        gamePlayer2.setEnergy(3);
        gamePlayer2.setUser(player2);

        gamePlayers = List.of(gamePlayer1,gamePlayer2);

        game = new Game();
        game.setId(1);
        game.setRound(1);
        game.setWinner(null);
        game.setStartedAt(java.time.LocalDateTime.now());
        game.setEndedAt(null);
        game.setGamePlayers(gamePlayers);

        game2 = new Game();
        game.setId(2);
        game2.setRound(10);
        game2.setWinner(player2);
        game.setStartedAt(java.time.LocalDateTime.now());
        game.setEndedAt(java.time.LocalDateTime.now());
        game2.setGamePlayers(gamePlayers);

    }

    @Test
    @WithMockUser("player")
    void findAllGamesTest() throws Exception {
        when(
            this.gameService.findAll()
        ).thenReturn(
            List.of(game, game2));
        mockMvc.perform(get(BASE_URL + "/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[*].id").exists())
               .andExpect(jsonPath("$[*].round").exists())
               .andExpect(jsonPath("$[*].startedAt").isNotEmpty())
               .andExpect(jsonPath("$[*].endedAt").isNotEmpty())
               .andExpect(jsonPath("$[*].winner").exists())
               .andExpect(jsonPath("$[*].gamePlayers").exists());
    }

    @Test
    @WithMockUser("player")
    void findByIdTest() throws Exception {
        when(
            this.gameService.getGameById(1)
        ).thenReturn(
            this.game);
        mockMvc.perform(get(BASE_URL + "/{id}", 1))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(game.getId()))
               .andExpect(jsonPath("$.round").value(game.getRound()))
               .andExpect(jsonPath("$.startedAt").isNotEmpty())
               .andExpect(jsonPath("$.endedAt").isNotEmpty())
               .andExpect(jsonPath("$.winner").doesNotExist())
               .andExpect(jsonPath("$.gamePlayers", hasSize(game.getGamePlayers().size())));
    }
}
