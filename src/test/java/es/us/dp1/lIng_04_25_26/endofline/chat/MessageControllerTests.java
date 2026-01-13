package es.us.dp1.lIng_04_25_26.endofline.chat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
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

import es.us.dp1.lIng_04_25_26.endofline.chat.dto.MessageRequestDTO;
import es.us.dp1.lIng_04_25_26.endofline.chat.dto.MessageResponseDTO;
import es.us.dp1.lIng_04_25_26.endofline.configuration.SecurityConfiguration;
import es.us.dp1.lIng_04_25_26.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.game.GameService;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

@WebMvcTest(controllers = MessageController.class)
class MessageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageRepository messageRepository;

    private static final String BASE_URL = "/api/v1/messages";
    private User testUser;
    private Game testGame;
    private MessageRequestDTO validRequest;
    private MessageResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("player1");

        testGame = new Game();
        testGame.setId(100);

        validRequest = new MessageRequestDTO("Hello World");

        responseDTO = new MessageResponseDTO(1, "player1", "Hello World", Instant.now().toEpochMilli());
    }


    @Test
    @WithMockUser(username = "player1")
    void testSaveMessageSuccessfully() throws Exception {
        when(userService.findCurrentUser()).thenReturn(testUser);
        when(gameService.getGameById(100)).thenReturn(testGame);
        when(messageService.saveMessage(any(MessageRequestDTO.class), eq(testUser), eq(testGame)))
            .thenReturn(responseDTO);

        mockMvc.perform(post(BASE_URL + "/100")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.sender").value("player1"))
            .andExpect(jsonPath("$.body").value("Hello World"));
    }


    @Test
    @WithMockUser(username = "player1")
    void testNotSaveMessage_ValidationErrors() throws Exception {
        MessageRequestDTO invalidRequest = new MessageRequestDTO("");

        mockMvc.perform(post(BASE_URL + "/100")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "player1")
    void testNotSaveMessage_GameNotFound() throws Exception {

        when(userService.findCurrentUser()).thenReturn(testUser);
        when(gameService.getGameById(999)).thenThrow(new ResourceNotFoundException("Game not found"));

        mockMvc.perform(post(BASE_URL + "/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "player1")
    void testGetMessages_WithoutSince() throws Exception {
        List<MessageResponseDTO> messages = List.of(responseDTO);
        when(messageService.getMessagesBySince(100, null)).thenReturn(messages);

        mockMvc.perform(get(BASE_URL + "/100")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].body").value("Hello World"));
    }


    @Test
    @WithMockUser(username = "player1")
    void testGetMessages_WithSince() throws Exception {
        Long sinceTimestamp = 1700000000L;
        List<MessageResponseDTO> messages = List.of(responseDTO);
        
        when(messageService.getMessagesBySince(100, sinceTimestamp)).thenReturn(messages);

        mockMvc.perform(get(BASE_URL + "/100")
                .param("since", String.valueOf(sinceTimestamp))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1));
    }


    @Test
    @WithMockUser(username = "player1")
    void testHandleInvalidSinceParam() throws Exception {
        mockMvc.perform(get(BASE_URL + "/100")
                .param("since", "invalid-timestamp")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
