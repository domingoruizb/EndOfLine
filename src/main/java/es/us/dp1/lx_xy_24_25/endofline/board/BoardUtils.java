package es.us.dp1.lx_xy_24_25.endofline.board;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;
import es.us.dp1.lx_xy_24_25.endofline.game.Game;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.GamePlayer;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer.SkillUsage;
import es.us.dp1.lx_xy_24_25.endofline.gameplayer_cards.GamePlayerCard;

import java.util.List;
import java.util.Objects;

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

        Integer rowDelta = (pos2.row() - pos1.row() + BOARD_SIZE) % BOARD_SIZE;
        Integer colDelta = (pos2.col() - pos1.col() + BOARD_SIZE) % BOARD_SIZE;

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
        if (Objects.isNull(lastPlacedCard)) {
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

    public static List<Integer> getInitialValidIndexes (Boolean isHost) {
        List<Integer> startPos = isHost ? INITIAL_POSITIONS.get(0) : INITIAL_POSITIONS.get(1);

        return List.of(getIndex(startPos.get(0), startPos.get(1) - 1));
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

        List<Integer> startingIndexes = INITIAL_POSITIONS
            .stream()
            .map(pos -> getIndex(pos.get(0), pos.get(1)))
            .toList();

        return potential
            .stream()
            .filter(index -> getIsIndexEmpty(index, board))
            .filter(index -> !startingIndexes.contains(index))
            .toList();
    }

    public static Boolean getIsTurnFinished(GamePlayer gamePlayer) {
        Game game = gamePlayer.getGame();
        Integer currentRound = game.getRound();

        // Default card limit
        Integer cardLimitInTurn = currentRound == 1 ? 1 : 2;

        // Check if player used a skill this round
        Skill skillThisRound = gamePlayer.getSkillsUsed().stream()
            .filter(usage -> usage.getRound().equals(currentRound))
            .map(SkillUsage::getSkill)
            .findFirst()
            .orElse(null);

        if (skillThisRound == Skill.BRAKE) {
            cardLimitInTurn = 1;
        } else if (skillThisRound == Skill.SPEED_UP) {
            cardLimitInTurn = 3;
        }

        return gamePlayer.getCardsPlayedThisRound() >= cardLimitInTurn;
    }

}
