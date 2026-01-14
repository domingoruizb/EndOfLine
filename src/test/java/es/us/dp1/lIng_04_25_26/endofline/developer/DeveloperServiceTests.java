package es.us.dp1.lIng_04_25_26.endofline.developer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.maven.model.Developer;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import es.us.dp1.lIng_04_25_26.endofline.developers.DeveloperService;

class DeveloperServiceTests {

    @Test
    void getDevelopers_firstPage_hasConsistentMetadata() {
        DeveloperService service = new DeveloperService();

        Page<Developer> page = service.getDevelopers(PageRequest.of(0, 1));

        assertEquals(0, page.getNumber());
        assertEquals(1, page.getSize());
        assertTrue(page.getTotalElements() >= 0);
        assertTrue(page.getContent().size() <= 1);
    }

    @Test
    void getDevelopers_offsetBeyondTotal_returnsEmptyContent() {
        DeveloperService service = new DeveloperService();

        Page<Developer> first = service.getDevelopers(PageRequest.of(0, 1));
        long total = first.getTotalElements();

        int pageIndex = (int) total + 1;
        Page<Developer> out = service.getDevelopers(PageRequest.of(pageIndex, 1));

        assertTrue(out.getContent().isEmpty());
        assertEquals(total, out.getTotalElements());
    }

    @Test
    void getDevelopers_secondCall_sameTotal_noException() {
        DeveloperService service = new DeveloperService();

        Page<Developer> a = service.getDevelopers(PageRequest.of(0, 1));
        Page<Developer> b = service.getDevelopers(PageRequest.of(0, 2));

        assertEquals(a.getTotalElements(), b.getTotalElements());
    }

}
