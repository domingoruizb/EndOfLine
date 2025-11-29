package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BoardUtils {

    public static final List<List<Integer>> INITIAL_POSITIONS = List.of(List.of(2, 4), List.of(4, 4));
    public static final Integer BOARD_SIZE = 7;

    public static String getName (GamePlayerCard gamePlayerCard) {
        return gamePlayerCard
            .getCard()
            .getImage()
            .replace("/cardImages/", "")
            .replace(".png", "");
    }

    public static List<Integer> getInitialValidIndexes (Boolean isHost) {
        List<Integer> startPos = isHost ? INITIAL_POSITIONS.get(0) : INITIAL_POSITIONS.get(1);

        return List.of(getIndex(startPos.get(0), startPos.get(1) - 1));
    }

    public static Integer getIndex (Integer row, Integer col) {
        return row + col * BOARD_SIZE;
    }

    public static BoardPosition getCoordinates (Integer index) {
        Integer row = index % BOARD_SIZE;
        Integer col = (int) Math.floor((double) index / BOARD_SIZE);

        return new BoardPosition(row, col);
    }

    public static Integer getNameToBinary (String name) {
        String binary = name.split("_")[1];

        return Integer.parseInt(binary, 2);
    }

    public static Boolean getIsIndexEmpty (Integer index, List<GamePlayerCard> board) {
        return board
            .stream()
            .noneMatch(card ->
                getIndex(
                    card.getPositionX(),
                    card.getPositionY()
                )
                    .equals(index)
            );
    }

    public static Integer getRotatedBits (Integer bits, Integer rotation) {
        Integer absRotation = Math.abs(rotation);

        return (((bits << absRotation) | (bits >>> (4 - absRotation))) >>> 0) & 0b1111;
    }

    public static Boolean getIsConnected (GamePlayerCard card1, GamePlayerCard card2) {
        BoardPosition pos1 = new BoardPosition(card1.getPositionX(), card1.getPositionY());
        BoardPosition pos2 = new BoardPosition(card2.getPositionX(), card2.getPositionY());

        Integer rowDelta = (pos1.row() - pos2.row() + BOARD_SIZE) % BOARD_SIZE;
        Integer colDelta = (pos1.col() - pos2.col() + BOARD_SIZE) % BOARD_SIZE;

        Integer bits1 = getNameToBinary(getName(card1));
        Integer rotatedBits1 = getRotatedBits(bits1, card1.getRotation());

        BoardRotation rotation = new BoardRotation(rotatedBits1);

        if (rowDelta == 1 && colDelta == 0) {
            return rotation.right() != 0;
        } else if (rowDelta == BOARD_SIZE - 1 && colDelta == 0) {
            return rotation.left() != 0;
        } else if (rowDelta == 0 && colDelta == 1) {
            return rotation.down() != 0;
        } else if (rowDelta == 0 && colDelta == BOARD_SIZE - 1) {
            return rotation.up() != 0;
        }

        return false;
    }

    public static Integer getRotation (Integer index, GamePlayerCard lastPlacedCard) {
        String name = getName(lastPlacedCard);

        if (name.equals("START")) {
            return 0;
        }

        BoardPosition selectedPos = getCoordinates(index);
        BoardPosition lastPos = new BoardPosition(
            lastPlacedCard.getPositionX(),
            lastPlacedCard.getPositionY()
        );

        Integer rowDelta = (selectedPos.row() - lastPos.row() + BOARD_SIZE) % BOARD_SIZE;
        Integer colDelta = (selectedPos.col() - lastPos.col() + BOARD_SIZE) % BOARD_SIZE;

        if (rowDelta == 1 && colDelta == 0) {
            return -3;
        } else if (rowDelta == BOARD_SIZE - 1 && colDelta == 0) {
            return -1;
        } else if (rowDelta == 0 && colDelta == 1) {
            return -2;
        } else if (rowDelta == 0 && colDelta == BOARD_SIZE - 1) {
            return 0;
        }

        return 0;
    }

    public static List<Integer> getValidIndexes (GamePlayerCard lastPlacedCard, List<GamePlayerCard> board) {
        String name = getName(lastPlacedCard);

        if (name.equals("START")) {
            return List.of();
        }

        Integer lastBits = getNameToBinary(name);
        Integer lastRotatedBits = getRotatedBits(lastBits, lastPlacedCard.getRotation());

        List<Integer> potential = new BoardRotation(lastRotatedBits)
            .getPotentialIndexes(lastPlacedCard);

        return potential
            .stream()
            .filter(index -> getIsIndexEmpty(index, board))
            .toList();
    }

}
