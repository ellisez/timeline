package com.idearfly.timeline.websocket;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public abstract class GameEndpoint extends Endpoint {
    protected Game game;
    protected Player player;

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
        if (player != null) {
            player.endpoint(null);
            player = null;
        }

    }

    public Game onJoinGame(Integer no) {
        game = gameCenter.game(no);
        if (game == null) {
            return null;
        }
        player = game.player(user);
        if (player != null) {
            try {
                player.endpoint().session.close();
            } catch (Exception e) {

            }
        } else {
            player = new Player();
            player.setUser(user);
            player.setImg(img);
            game.join(player);
        }
        player.endpoint(this);

        return game;

    }


    public Integer onLeaveGame() {
        if (game == null) {
            return null;
        }
        if (player != null) {
            game.leave(player);
            player.endpoint(null);
        }
        return game.getNo();
    }
}