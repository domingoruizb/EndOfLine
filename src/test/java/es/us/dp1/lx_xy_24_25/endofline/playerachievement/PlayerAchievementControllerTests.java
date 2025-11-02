package es.us.dp1.lx_xy_24_25.endofline.playerachievement;

import es.us.dp1.lx_xy_24_25.endofline.friendship.FriendshipRestController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;

@WebMvcTest(controllers = FriendshipRestController.class, excludeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PlayerAchievementControllerTests {

    private static final String BASE_URL = "/api/v1/friendships";
    private static final Integer TEST_FRIENDSHIP_ID = 1;
    private static final Integer TEST_USER_ID = 1;


}
