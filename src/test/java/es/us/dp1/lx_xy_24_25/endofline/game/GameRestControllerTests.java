package es.us.dp1.lx_xy_24_25.endofline.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import es.us.dp1.lx_xy_24_25.endofline.enums.CardStatus;
import es.us.dp1.lx_xy_24_25.endofline.enums.Color;
import es.us.dp1.lx_xy_24_25.endofline.enums.Orientation;
import es.us.dp1.lx_xy_24_25.endofline.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameRestControllerTests {
    
}
