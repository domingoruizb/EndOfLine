package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final List<List<Integer>> INITIAL_POSITIONS = List.of(List.of(2, 4), List.of(4, 4));

    public List<Integer> getInitialValidIndexes (Boolean isHost) {
        List<Integer> startPos = isHost ? INITIAL_POSITIONS.get(0) : INITIAL_POSITIONS.get(1);

        return List.of(BoardUtils.getIndex(startPos.get(0), startPos.get(1) - 1));
    }

    public Integer getRotation (Integer index, GamePlayerCard lastPlacedCard) {
        String name = BoardUtils.getName(lastPlacedCard);

        if (name.equals("START")) {
            return 0;
        }

        BoardPosition selectedPos = BoardUtils.getCoordinates(index);
        BoardPosition lastPos = new BoardPosition(
            lastPlacedCard.getPositionX(),
            lastPlacedCard.getPositionY()
        );

        int rowDelta = (selectedPos.row() - lastPos.row() + BoardUtils.BOARD_SIZE) % BoardUtils.BOARD_SIZE;
        int colDelta = (selectedPos.col() - lastPos.col() + BoardUtils.BOARD_SIZE) % BoardUtils.BOARD_SIZE;

        if (rowDelta == 1 && colDelta == 0) {
            return -3;
        } else if (rowDelta == BoardUtils.BOARD_SIZE - 1 && colDelta == 0) {
            return -1;
        } else if (rowDelta == 0 && colDelta == 1) {
            return -2;
        } else if (rowDelta == 0 && colDelta == BoardUtils.BOARD_SIZE - 1) {
            return 0;
        }

        return 0;
    }

    public List<Integer> getValidIndexes (GamePlayerCard lastPlacedCard, List<GamePlayerCard> board) {
        String name = BoardUtils.getName(lastPlacedCard);

        if (name.equals("START")) {
            return List.of();
        }

        Integer lastBits = BoardUtils.getNameToBinary(name);
        Integer lastRotatedBits = BoardUtils.getRotatedBits(lastBits, lastPlacedCard.getRotation());

        List<Integer> potential = new BoardRotation(lastRotatedBits)
            .getPotentialIndexes(lastPlacedCard);

        return potential
            .stream()
            .filter(index -> BoardUtils.getIsIndexEmpty(index, board))
            .toList();
    }

    public Boolean getIsPlacementValid (
        Integer selectedIndex,
        GamePlayerCard selectedCard,
        GamePlayerCard lastPlacedCard,
        List<GamePlayerCard> lastPlacedCards,
        List<GamePlayerCard> board,
        Boolean isHost
    ) {
        if (selectedCard == null) {
            return false;
        }

        if (lastPlacedCards.isEmpty()) {
            if (isHost == null) {
                return false;
            }

            if (!getInitialValidIndexes(isHost).contains(selectedIndex)) {
                return false;
            }
        }

        BoardPosition coordinates = BoardUtils.getCoordinates(selectedIndex);
        boolean isStart = INITIAL_POSITIONS
            .stream()
            .anyMatch(pos ->
                pos.getFirst().equals(coordinates.row()) &&
                pos.getLast().equals(coordinates.col())
            );

        if (lastPlacedCard != null) {
            List<Integer> validIndexes = getValidIndexes(lastPlacedCard, board);

            if (!validIndexes.contains(selectedIndex)) {
                return false;
            }
        }

        return !isStart && BoardUtils.getIsIndexEmpty(selectedIndex, board);
    }

    public GamePlayerCard getReverseCard (GamePlayerCard lastPlacedCard, List<GamePlayerCard> lastPlacedCards) {
        if (lastPlacedCards.size() < 2) {
            return null;
        }

        for (int i = lastPlacedCards.size() - 2; i >= 0; i--) {
            GamePlayerCard candidate = lastPlacedCards.get(i);

            if (BoardUtils.getIsConnected(candidate, lastPlacedCard)) {
                return candidate;
            }
        }

        return null;
    }

    public Boolean getCanReverse (
        List<GamePlayerCard> lastPlacedCards,
        GamePlayerCard lastPlacedCard,
        List<GamePlayerCard> board
    ) {
        GamePlayerCard reverseCard = getReverseCard(lastPlacedCard, lastPlacedCards);

        if (reverseCard == null) {
            return false;
        }

        List<Integer> validIndexes = getValidIndexes(reverseCard, board);
        return !validIndexes.isEmpty();
    }
}
