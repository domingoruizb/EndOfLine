package es.us.dp1.lx_xy_24_25.endofline.achievement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import es.us.dp1.lx_xy_24_25.endofline.user.UserService;
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

import es.us.dp1.lx_xy_24_25.endofline.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.endofline.achievement.AchievementRestController;
import es.us.dp1.lx_xy_24_25.endofline.achievement.AchievementService;
import es.us.dp1.lx_xy_24_25.endofline.user.Authorities;
import es.us.dp1.lx_xy_24_25.endofline.achievement.Category;
import es.us.dp1.lx_xy_24_25.endofline.user.User;

@WebMvcTest(controllers = AchievementRestController.class, excludeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))

class AchievementControllerTests {

    private static final String BASE_URL = "/api/v1/achievements";
    public static final Integer TEST_ACHIEVEMENT_ID = 1;
    private static final String avatar = "https://cdn-icons-png.flaticon.com/512/147/147144.png";
    private static final String badgeImage = "https://cdn-icons-png.flaticon.com/128/5730/5730459.png";

    @SuppressWarnings("unused")
    @Autowired
    private AchievementRestController achievementRestController;

    @MockBean
    private AchievementService achievementService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
	private ObjectMapper objectMapper;

    private Achievement achievement;
    private Achievement achievement2;
    private User admin, player;
    private Authorities authority, authority2;

    private User createUser(Integer id, Boolean isAdmin) {
        User user = new User();
        user.setId(id);
        user.setName("Name" + id);
        user.setSurname("Surname" + id);
        user.setPassword("Player" + id + "!");
        user.setEmail("player" + id + "@gmail.com");
        user.setBirthdate(LocalDate.of(1999, 01, 01));
        if (isAdmin) {
            user.setAuthority(authority);
        } else {
            user.setAuthority(authority2);
        }
        user.setAvatar(avatar);
        return user;
    }

    @BeforeEach
    void setUp() {
        authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("ADMIN");

        authority2 = new Authorities();
        authority.setId(2);
        authority.setAuthority("PLAYER");

        admin = createUser(1, true);
        player = createUser(2, false);

        achievement = new Achievement();
        achievement.setId(1);
        achievement.setName("achievementName");
        achievement.setDescription("achievementDescription");
        achievement.setBadgeImage(badgeImage);
        achievement.setThreshold(10.0);
        achievement.setCategory(Category.VICTORIES);

        achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setName("achievementName2");
        achievement2.setDescription("achievementDescription2");
        achievement2.setBadgeImage(badgeImage);
        achievement2.setThreshold(20.0);
        achievement2.setCategory(Category.VICTORIES);
    }

    @Test
    @WithMockUser("admin")
    void adminFindAllAchievementsTest() throws Exception {
        when(this.achievementService.getAchievements()).thenReturn(List.of(achievement, achievement2));

        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("achievementName"))
                .andExpect(jsonPath("$[?(@.id == 2)].name").value("achievementName2"));
    }

    @Test
    @WithMockUser("player")
    void playerFindAllAchievementsTest() throws Exception {
        when(this.achievementService.getAchievements()).thenReturn(List.of(achievement, achievement2));

        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.id == 1)].name").value("achievementName"))
                .andExpect(jsonPath("$[?(@.id == 2)].name").value("achievementName2"));
    }

    @Test
    @WithMockUser("admin")
    void adminFindAchievementByIdTest() throws Exception {
        when(this.achievementService.getById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);

        mockMvc.perform(get(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("achievementName"));
    }

    @Test
    @WithMockUser("player")
    void playerFindAchievementByIdTest() throws Exception {
        when(this.achievementService.getById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);

        mockMvc.perform(get(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("achievementName"));
    }

    @Test
    @WithMockUser("admin")
    void adminCreateAchievementTest() throws Exception {
        Achievement achievement3 = new Achievement();
        achievement3.setId(3);
        achievement3.setName("achievementName3");
        achievement3.setDescription("achievementDescription3");
        achievement3.setBadgeImage(badgeImage);
        achievement3.setThreshold(30.0);
        achievement3.setCategory(Category.VICTORIES);

        mockMvc.perform(post(BASE_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(achievement3))).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("admin")
    void adminUpdateAchievementTest() throws Exception {
        achievement.setName("UPDATED");
        achievement.setDescription("DESCRIPTION UPDATED");

        when(this.achievementService.getById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);
        when(this.achievementService.updateAchievement(any(Integer.class), any(Achievement.class))).thenReturn(achievement);

        mockMvc.perform(put(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(achievement))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"))
                .andExpect(jsonPath("$.description").value("DESCRIPTION UPDATED"));
    }

    @Test
    @WithMockUser("admin")
    void adminDeleteAchievementTest() throws Exception {
        when(this.achievementService.getById(TEST_ACHIEVEMENT_ID)).thenReturn(achievement);

        doNothing().when(this.achievementService).deleteAchievementById(TEST_ACHIEVEMENT_ID);
        mockMvc.perform(delete(BASE_URL + "/{id}", TEST_ACHIEVEMENT_ID).with(csrf()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Achievement deleted!"));
    }

}

