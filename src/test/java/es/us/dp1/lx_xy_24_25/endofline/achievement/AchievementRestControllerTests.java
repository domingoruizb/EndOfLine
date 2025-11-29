package es.us.dp1.lx_xy_24_25.endofline.achievement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import org.springframework.http.MediaType;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.endofline.achievement.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(
        controllers = AchievementRestController.class
)
@AutoConfigureMockMvc(addFilters = true)  // 🔥 Activa seguridad REAL en los tests
public class AchievementRestControllerTests {

    private static final String BASE_URL = "/api/v1/achievements";
    private static final String BADGE_IMAGE = "https://cdn-icons-png.flaticon.com/128/5730/5730459.png";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AchievementService achievementService;

    private Achievement a1;
    private Achievement a2;

    @BeforeEach
    void setup() {
        a1 = new Achievement();
        a1.setId(1);
        a1.setName("Achievement1");
        a1.setDescription("Description1");
        a1.setBadgeImage(BADGE_IMAGE);
        a1.setThreshold(10.0);
        a1.setCategory(Category.VICTORIES);

        a2 = new Achievement();
        a2.setId(2);
        a2.setName("Achievement2");
        a2.setDescription("Description2");
        a2.setBadgeImage(BADGE_IMAGE);
        a2.setThreshold(20.0);
        a2.setCategory(Category.GAMES_PLAYED);
    }

    @Test
    @WithMockUser(authorities = "PLAYER")
    void testFindAllAchievementsAsPlayer() throws Exception {
        when(achievementService.getAchievements()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("Achievement1"))
                .andExpect(jsonPath("$[?(@.id == 2)].name").value("Achievement2"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testFindAllAchievementsAsAdmin() throws Exception {
        when(achievementService.getAchievements()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @WithMockUser(authorities = "PLAYER")
    void testFindAllEmpty() throws Exception {
        when(achievementService.getAchievements()).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @WithMockUser(authorities = "PLAYER")
    void testFindAchievementById() throws Exception {
        when(achievementService.getById(1)).thenReturn(a1);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Achievement1"));
    }

    @Test
    @WithMockUser(authorities = "PLAYER")
    void testFindAchievementByIdNotFound() throws Exception {
        when(achievementService.getById(99))
                .thenThrow(new ResourceNotFoundException("Achievement not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testCreateAchievement() throws Exception {
        when(achievementService.saveAchievement(any(Achievement.class))).thenReturn(a1);

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
/*
    @Test
    @WithMockUser(authorities = "PLAYER")
    void testPlayerCannotCreate() throws Exception {
        Achievement achievement3 = new Achievement();
        achievement3.setId(3);
        achievement3.setName("achievementName3");
        achievement3.setDescription("achievementDescription3");
        achievement3.setBadgeImage("img");
        achievement3.setThreshold(30.0);
        achievement3.setCategory(Category.VICTORIES);

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(achievement3)))
                .andExpect(status().isForbidden());
    }
*/

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testCreateInvalidAchievement() throws Exception {

        Achievement invalid = new Achievement();
        invalid.setId(99);
        invalid.setName("");
        invalid.setDescription("");
        invalid.setCategory(null);
        invalid.setThreshold(-5);

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateAchievement() throws Exception {

        a1.setName("UPDATED");
        when(achievementService.getById(1)).thenReturn(a1);
        when(achievementService.updateAchievement(any(), any())).thenReturn(a1);

        mockMvc.perform(put(BASE_URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateNotFound() throws Exception {

        when(achievementService.getById(99))
                .thenThrow(new ResourceNotFoundException("Achievement not found"));

        mockMvc.perform(put(BASE_URL + "/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a1)))
                .andExpect(status().isNotFound());
    }
/*
    @Test
    @WithMockUser(authorities = "PLAYER")
    void testPlayerCannotUpdateAchievement() throws Exception {

        a1.setName("UPDATED");

        mockMvc.perform(put(BASE_URL + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a1)))
                .andExpect(status().isForbidden());
    }
*/

    // ================================
    // DELETE (ADMIN ONLY)
    // ================================

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteAchievement() throws Exception {

        when(achievementService.getById(1)).thenReturn(a1);
        doNothing().when(achievementService).deleteAchievementById(1);

        mockMvc.perform(delete(BASE_URL + "/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Achievement deleted!"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteAchievementNotFound() throws Exception {
        when(achievementService.getById(99))
                .thenThrow(new ResourceNotFoundException("Achievement not found"));

        mockMvc.perform(delete(BASE_URL + "/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
/*
    @Test
    @WithMockUser(authorities = "PLAYER") // PLAYER NO PUEDE ELIMINAR
    void testPlayerCannotDeleteAchievement() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/1")
                .with(csrf()))
                .andExpect(status().isForbidden()); // ✔ 403
    }
*/
}
