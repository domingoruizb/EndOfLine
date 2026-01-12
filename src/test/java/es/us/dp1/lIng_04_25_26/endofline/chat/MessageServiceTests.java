package es.us.dp1.lIng_04_25_26.endofline.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lIng_04_25_26.endofline.chat.dto.MessageRequestDTO;
import es.us.dp1.lIng_04_25_26.endofline.chat.dto.MessageResponseDTO;
import es.us.dp1.lIng_04_25_26.endofline.game.Game;
import es.us.dp1.lIng_04_25_26.endofline.user.User;

@ExtendWith(MockitoExtension.class)
class MessageServiceTests {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private User testUser;
    private Game testGame;
    private Message testMessage;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("player1");

        testGame = new Game();
        testGame.setId(100);

        testMessage = new Message();
        testMessage.setId(10);
        testMessage.setBody("Que pasa quillo");
        testMessage.setSentAt(Instant.now().toEpochMilli());
        testMessage.setUser(testUser);
        testMessage.setGame(testGame);
    }

    @Test
    void shouldSaveMessage() {
        MessageRequestDTO request = new MessageRequestDTO("  Que pasa quillo  ");
        
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        MessageResponseDTO result = messageService.saveMessage(request, testUser, testGame);

        verify(messageRepository, times(1)).save(any(Message.class));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10);
        assertThat(result.getBody()).isEqualTo("Que pasa quillo");
        assertThat(result.getSender()).isEqualTo("player1");
    }

    @Test
    void shouldGetMessagesBySince() {
        Integer gameId = 100;
        Long since = 1600000000L;
        
        when(messageRepository.findMessages(gameId, since)).thenReturn(List.of(testMessage));

        List<MessageResponseDTO> result = messageService.getMessagesBySince(gameId, since);

        verify(messageRepository, times(1)).findMessages(gameId, since);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBody()).isEqualTo("Que pasa quillo");
        assertThat(result.get(0).getSender()).isEqualTo("player1");
    }

    @Test
    void shouldGetMessagesBySince_WithNullSince() {
        Integer gameId = 100;
        Long since = null;
        
        when(messageRepository.findMessages(gameId, since)).thenReturn(List.of(testMessage));

        List<MessageResponseDTO> result = messageService.getMessagesBySince(gameId, since);

        verify(messageRepository).findMessages(gameId, null);
        assertThat(result).isNotEmpty();
    }
    
    @Test
    void shouldGetMessagesBySince_ReturnEmptyList() {
        Integer gameId = 100;
        Long since = 9999999999L;
        
        when(messageRepository.findMessages(gameId, since)).thenReturn(List.of());

        List<MessageResponseDTO> result = messageService.getMessagesBySince(gameId, since);

        assertThat(result).isEmpty();
    }
}