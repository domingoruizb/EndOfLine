package es.us.dp1.lIng_04_25_26.endofline.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageDTO<T> {

    private List<T> content;
    private Integer page;
    private Integer size;

    private Long total;
    private Integer pages;

    private Boolean hasPrevious;
    private Boolean hasNext;

    public PageDTO(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = page.getTotalElements();
        this.pages = page.getTotalPages();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
    }

}
