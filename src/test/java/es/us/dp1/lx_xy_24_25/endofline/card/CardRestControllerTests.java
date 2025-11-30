package es.us.dp1.lx_xy_24_25.endofline.card;

import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayerService;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardDTO;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCardService;
import es.us.dp1.lx_xy_24_25.endofline.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private GamePlayerCardService gamePlayerCardService;

    @MockBean
    private GamePlayerService gamePlayerService;

    private Card sampleCard;

    @BeforeEach
    void setup() {
        sampleCard = new Card();
        sampleCard.setId(1);
        sampleCard.setImage("card_red.png");
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testFindAllCards() throws Exception {
        List<Card> cards = List.of(new Card());
        when(cardService.findAll()).thenReturn(cards);

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testFindByImage() throws Exception {
        Card card = new Card();
        card.setImage("red0.png");
        when(cardService.findByImage("red0.png")).thenReturn(card);

        mockMvc.perform(get("/api/v1/cards/image/red0.png"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image").value("red0.png"));
    }


    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testFindByColor() throws Exception {
        when(cardService.getCardsByColor("RED")).thenReturn(List.of(new Card()));

        mockMvc.perform(get("/api/v1/cards/color/RED"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testFindByGameId() throws Exception {
        when(cardService.findByGameId(1)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/cards/game/1"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"PLAYER"})
    void testPlaceCardSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setId(99);
        mockUser.setUsername("testuser");
        GamePlayer gp = new GamePlayer();
        gp.setId(1);
        gp.setUser(mockUser);
        Card card = new Card();
        card.setId(10);
        card.setImage("card_red_01");
        card.setColor(Color.RED);
        card.setInitiative(3);

        GamePlayerCardDTO dto = new GamePlayerCardDTO();
        dto.setImage("card_red_01");
        dto.setTurnFinished(true);
        GamePlayerCard gpc = new GamePlayerCard();
        gpc.setId(50);
        gpc.setGamePlayer(gp);
        gpc.setCard(card);

        when(gamePlayerService.getById(1)).thenReturn(gp);
        when(gamePlayerService.isValidTurn(gp)).thenReturn(true);
        when(cardService.findByImage("card_red_01")).thenReturn(card);
        when(gamePlayerCardService.placeCard(any(GamePlayerCard.class), eq(true)))
                .thenReturn(gpc);
        mockMvc.perform(
                post("/api/v1/cards/place/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "image": "card_red_01",
                                    "turnFinished": true
                                }
                                """)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(50));
    }


    @Test
    void testPlaceCardInvalidTurn() throws Exception {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setId(100);

        when(gamePlayerService.getById(100)).thenReturn(gamePlayer);
        when(gamePlayerService.isValidTurn(gamePlayer)).thenReturn(false);

        String jsonBody = """
                {
                    "image": "card_red.png",
                    "turnFinished": true
                }
                """;

        mockMvc.perform(post("/api/v1/cards/place/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isForbidden());
    }
}
