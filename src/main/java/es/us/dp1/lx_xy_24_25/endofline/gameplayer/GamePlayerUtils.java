package es.us.dp1.lx_xy_24_25.endofline.gameplayer;

import es.us.dp1.lx_xy_24_25.endofline.enums.Skill;

public class GamePlayerUtils {

    public static Boolean isValidTurn (GamePlayer gamePlayer) {
        return gamePlayer.getGame().getTurn().equals(gamePlayer.getUser().getId());
    }

    public static Boolean isHost (GamePlayer gamePlayer) {
        return gamePlayer.getGame().getHost().getId().equals(gamePlayer.getUser().getId());
    }

    public static Boolean isSkillAvailable (GamePlayer gamePlayer) {
        Boolean isTurn = isValidTurn(gamePlayer);
        Integer round = gamePlayer.getGame().getRound();
        Skill skill = isTurn ? gamePlayer.getGame().getSkill() : null;

        return isTurn && round > 1 && skill == null && gamePlayer.getCardsPlayedThisRound() == 0 && gamePlayer.getEnergy() > 0;
    }

    public static Boolean isDeckChangeAvailable (GamePlayer gamePlayer) {
        return gamePlayer.getDeckRequests() < 2 && gamePlayer.getGame().getRound() < 2;
    }

}
