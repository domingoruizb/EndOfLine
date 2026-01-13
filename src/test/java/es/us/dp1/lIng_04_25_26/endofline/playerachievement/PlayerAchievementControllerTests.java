package es.us.dp1.lIng_04_25_26.endofline.playerachievement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.us.dp1.lIng_04_25_26.endofline.achievement.Achievement;
import es.us.dp1.lIng_04_25_26.endofline.configuration.SecurityConfiguration;
import es.us.dp1.lIng_04_25_26.endofline.user.User;
import es.us.dp1.lIng_04_25_26.endofline.user.UserService;

@WebMvcTest(controllers = PlayerAchievementController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration = SecurityConfiguration.class)
class PlayerAchievementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerAchievementService playerAchievementService;

    @MockBean
    private UserService userService;

    private User user;
    private Achievement achievement;
    private PlayerAchievement playerAchievement;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testPlayer");
        user.setPassword("password");

        achievement = new Achievement();
        achievement.setId(100);
        achievement.setName("Test Achievement");

        playerAchievement = PlayerAchievement.build(user, achievement, LocalDateTime.now());
        playerAchievement.setId(1);
    }


    @Test
    @WithMockUser(username = "testPlayer", authorities = {"PLAYER"})
    void testFindAllPlayerAchievementsForCurrentUser() throws Exception {
        given(userService.findCurrentUser()).willReturn(user);
        given(playerAchievementService.findAllByUserId(user.getId())).willReturn(List.of(playerAchievement));

        mockMvc.perform(get("/api/v1/playerachievements")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].achievementId", is(100)))
                .andExpect(jsonPath("$[0].userName", is("testPlayer")));
    }


    @Test
    @WithMockUser(username = "testPlayer", authorities = {"PLAYER"})
    void testEmptyListWhenNoAchievements() throws Exception {
        given(userService.findCurrentUser()).willReturn(user);
        given(playerAchievementService.findAllByUserId(user.getId())).willReturn(List.of());

        mockMvc.perform(get("/api/v1/playerachievements")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testFindPlayerAchievementsById() throws Exception {
        given(playerAchievementService.findAllByAchievementId(100)).willReturn(List.of(playerAchievement));

        mockMvc.perform(get("/api/v1/playerachievements/achievement/{achievementId}", 100)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].achievementId", is(100)));
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testReturnEmptyListIfAchievementHasNotBeenObtainedByAnyone() throws Exception {
        given(playerAchievementService.findAllByAchievementId(999)).willReturn(List.of());

        mockMvc.perform(get("/api/v1/playerachievements/achievement/{achievementId}", 999)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    @WithMockUser(username = "testPlayer", authorities = {"PLAYER"})
    void testReturnUnlockedAchievementIds() throws Exception {
        given(userService.findCurrentUser()).willReturn(user);
        given(playerAchievementService.findAchievementIdsByUserId(user.getId())).willReturn(List.of(100, 101));

        mockMvc.perform(get("/api/v1/playerachievements/ids")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ids", hasSize(2)))
                .andExpect(jsonPath("$.ids[0]", is(100)))
                .andExpect(jsonPath("$.ids[1]", is(101)));
    }


    @Test
    void testReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/playerachievements"))
                .andExpect(status().isUnauthorized());
    }

}