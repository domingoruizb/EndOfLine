package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;

import java.util.ArrayList;
import java.util.List;

public record BoardRotation(Integer bits) {

    public Integer left () {
        return bits & 0b1000;
    }

    public Integer up () {
        return bits & 0b0100;
    }

    public Integer right () {
        return bits & 0b0010;
    }

    public Integer down () {
        return bits & 0b0001;
    }

    public List<Integer> getPotentialIndexes (GamePlayerCard lastPlacedCard) {
        BoardPosition lastPos = new BoardPosition(
            lastPlacedCard.getPositionX(),
            lastPlacedCard.getPositionY()
        );

        List<Integer> potential = new ArrayList<>();

        if (left() > 0) {
            Integer newRow = (lastPos.row() - 1 + BoardUtils.BOARD_SIZE) % BoardUtils.BOARD_SIZE;
            potential.add(BoardUtils.getIndex(newRow, lastPos.col()));
        }

        if (right() > 0) {
            Integer newRow = (lastPos.row() + 1) % BoardUtils.BOARD_SIZE;
            potential.add(BoardUtils.getIndex(newRow, lastPos.col()));
        }

        if (up() > 0) {
            Integer newCol = (lastPos.col() - 1 + BoardUtils.BOARD_SIZE) % BoardUtils.BOARD_SIZE;
            potential.add(BoardUtils.getIndex(lastPos.row(), newCol));
        }

        if (down() > 0) {
            Integer newCol = (lastPos.col() + 1) % BoardUtils.BOARD_SIZE;
            potential.add(BoardUtils.getIndex(lastPos.row(), newCol));
        }

        return potential;
    }

}
