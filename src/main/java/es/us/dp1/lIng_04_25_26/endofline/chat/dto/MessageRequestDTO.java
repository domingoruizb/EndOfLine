package es.us.dp1.lIng_04_25_26.endofline.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MessageRequestDTO {

    @NotBlank
    @Length(max = 2000)
    public String text;

    public MessageRequestDTO(
        String text
    ) {
        this.text = text;
    }

    public String trimmedText() {
        return text.trim();
    }

}
