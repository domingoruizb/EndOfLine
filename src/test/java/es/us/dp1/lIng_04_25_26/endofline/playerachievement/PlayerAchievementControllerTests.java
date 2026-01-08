package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lIng_04_25_26.endofline.playerachievement.PlayerAchievementDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class PlayerAchievementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/playerachievements";

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testFindAll() throws Exception {
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", not(empty())))
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].userId", notNullValue()))
            .andExpect(jsonPath("$[0].achievementId", notNullValue()));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testFindById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userId").value(4))
            .andExpect(jsonPath("$.achievementId").value(1));
    }

    @Test
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/99999"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreate() throws Exception {
        String jsonBody = """
        {
          "userId": 4,
          "achievementId": 2,
          "achievedAt": "2024-12-01T10:00:00"
        }
        """;

        mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBody)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.userId").value(4))
            .andExpect(jsonPath("$.achievementId").value(2));
    }

    @Test
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    void testCreateUserNotFound() throws Exception {

        PlayerAchievementDTO dto = new PlayerAchievementDTO();
        dto.setUser_id(99999);
        dto.setAchievement_id(1);
        dto.setAchieved_at(LocalDateTime.now());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    void testCreateAchievementNotFound() throws Exception {

        PlayerAchievementDTO dto = new PlayerAchievementDTO();
        dto.setUser_id(4);
        dto.setAchievement_id(99999);
        dto.setAchieved_at(LocalDateTime.now());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testDelete() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testDeleteNonExisting() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9999"))
            .andExpect(status().isOk());
    }
}
