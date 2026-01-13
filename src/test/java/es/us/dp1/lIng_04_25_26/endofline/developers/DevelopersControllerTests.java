package es.us.dp1.lIng_04_25_26.endofline.developers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.maven.model.Developer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = DevelopersController.class)
class DevelopersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeveloperService developerService;

    private static final String BASE_URL = "/api/v1/developers";

    @Test
    @WithMockUser
    void shouldReturnDevelopersPage() throws Exception {
        Developer dev1 = new Developer();
        dev1.setId("dev1");
        dev1.setName("Dev One");

        Developer dev2 = new Developer();
        dev2.setId("dev2");
        dev2.setName("Dev Two");

        when(developerService.getDevelopers(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(dev1, dev2), PageRequest.of(0, 1), 2));

        mockMvc.perform(get(BASE_URL)
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(1))
            .andExpect(jsonPath("$.total").value(2))
            .andExpect(jsonPath("$.pages").value(2))
            .andExpect(jsonPath("$.hasPrevious").value(false))
            .andExpect(jsonPath("$.hasNext").value(true))
            .andExpect(jsonPath("$.content.length()").value(2));

        verify(developerService, times(1)).getDevelopers(any(Pageable.class));
    }

}
