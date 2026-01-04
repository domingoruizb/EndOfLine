package es.us.dp1.lx_xy_24_25.endofline.friendship;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
public class FriendshipControllerTests {

    private static final String BASE_URL = "/api/v1/friendships";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testFindMyFriendships() throws Exception {
        mockMvc.perform(get(BASE_URL + "/myFriendships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    void testFindPendingReceived() throws Exception {
        mockMvc.perform(get(BASE_URL + "/myPendingReceivedFriendships"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player10", authorities = {"PLAYER"})
    void testCreateFriendship() throws Exception {
        FriendshipDTO dto = new FriendshipDTO(13, 14);
        String json = mapper.writeValueAsString(dto);
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sender.id", is(13)))
                .andExpect(jsonPath("$.receiver.id", is(14)))
                .andExpect(jsonPath("$.friendState", is("PENDING")));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testCreateFriendshipWithSelf() throws Exception {
        FriendshipDTO dto = new FriendshipDTO(4, 4);
        String json = mapper.writeValueAsString(dto);
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("yourself")));
    }

    @Test
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    void testAcceptFriendship() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2/acceptFriendship"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendState", is("ACCEPTED")));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testAcceptFriendshipForbidden() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2/acceptFriendship"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "player6", authorities = {"PLAYER"})
    void testRejectFriendship() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9/rejectFriendship"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player4", authorities = {"PLAYER"})
    void testRejectFriendshipForbidden() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9/rejectFriendship"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void testDeleteFriendship() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player3", authorities = {"PLAYER"})
    void testDeleteFriendshipForbidden() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isForbidden());
    }
}
