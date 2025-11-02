package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.dp1.lx_xy_24_25.endofline.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.endofline.auth.AuthController;
import es.us.dp1.lx_xy_24_25.endofline.user.Authorities;
import es.us.dp1.lx_xy_24_25.endofline.user.User;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerAchievementRestController.class, excludeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PlayerAchievementControllerTests {

    private static final String BASE_URL = "/api/v1/playerachievements";

    @MockBean
    private AuthController authController;

    @MockBean
    private PlayerAchievementService playerAchievementService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User player;
    private Achievement achievement;
    private PlayerAchievement playerAchievement;

    @BeforeEach
    void setUp () throws Exception {
        Achievement achievement = new Achievement();
        achievement.setId(1);

        this.achievement = achievement;

        Authorities authorities = new Authorities();
        authorities.setId(1);
        authorities.setAuthority("PLAYER");

        User user = new User();
        user.setId(1);

        this.player = user;

        PlayerAchievement pa = new PlayerAchievement();
        pa.setId(1);
        pa.setUser(this.player);
        pa.setAchievement(achievement);
        pa.setAchievedAt(LocalDateTime.now());

        this.playerAchievement = pa;
    }

    @Test
    @WithMockUser("player")
    void testAllPlayerAchievements () throws Exception {
        when(
            this.playerAchievementService.findAll()
        ).thenReturn(
            List.of(this.playerAchievement)
        );

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[*].id").exists())
            .andExpect(jsonPath("$[*].user_id").exists())
            .andExpect(jsonPath("$[*].achievement_id").exists())
            .andExpect(jsonPath("$[*].achieved_at").exists());
    }

    @Test
    @WithMockUser("player")
    void testPlayerAchievementById () throws Exception {
        when(
            this.playerAchievementService.findById(this.playerAchievement.getId())
        ).thenReturn(
            this.playerAchievement
        );

        mockMvc.perform(get(BASE_URL + "/" + this.playerAchievement.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.user_id").exists())
            .andExpect(jsonPath("$.achievement_id").exists())
            .andExpect(jsonPath("$.achieved_at").exists());
    }

    @Test
    @WithMockUser("player")
    void testPlayerAchievementCreate () throws Exception {
        PlayerAchievementDTO dto = new PlayerAchievementDTO();
        dto.setId(1);
        dto.setUser_id(this.player.getId());
        dto.setAchievement_id(this.achievement.getId());
        dto.setAchieved_at(LocalDateTime.now());

        when(
            this.playerAchievementService.create(
                dto.getUser_id(),
                dto.getAchievement_id(),
                dto.getAchieved_at()
            )
        ).thenReturn(this.playerAchievement);

        mockMvc.perform(
            post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
            )
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("player")
    void testPlayerAchievementDelete() throws Exception {
        Integer id = this.playerAchievement.getId();

        mockMvc.perform(delete(BASE_URL + "/" + id).with(csrf()))
            .andExpect(status().isOk());
    }

}
