package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;

import java.util.List;

public class BoardUtils {

    public static final Integer BOARD_SIZE = 7;

    public static String getName (GamePlayerCard gamePlayerCard) {
        return gamePlayerCard
            .getCard()
            .getImage()
            .replace("/cardImages/", "")
            .replace(".png", "");
    }

    public static Integer getIndex (Integer row, Integer col) {
        return row + col * BOARD_SIZE;
    }

    public static BoardPosition getCoordinates (Integer index) {
        Integer row = index % BOARD_SIZE;
        Integer col = (int) Math.floor(index / BOARD_SIZE);

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

}
