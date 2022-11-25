package com.olyno.gamet.skript.expressions.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.olyno.gami.Gami;
import com.olyno.gami.models.Game;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

@Name("Game Expression")
@Description("Return a game from its name (the game must to exist)")
@Examples({
    "command game:" +
    "\ttrigger:" +
    "\t\tdelete game \"test\"" +
    "\t\tsend \"Game \"\"test\"\" deleted!\""
})
@Since("2.0")

public class ExprGame extends SimpleExpression<Game> {

    static {
        Skript.registerExpression(ExprGame.class, Game.class, ExpressionType.SIMPLE,
            "[the] [mini[(-| )]]game [%-string%]"
        );
    }

    public static Game lastGame;

    private Boolean scope = false;
    private Expression<String> gameName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init( Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult ) {
        gameName = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected Game[] get( Event e ) {
        String currentGameName = gameName.getSingle(e);
        if (scope) {
            return new Game[]{lastGame};
        }
        return Gami.getGameByName(currentGameName)
            .map(gameFound -> new Game[]{ gameFound })
            .orElse(new Game[0]);
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        switch (mode) {
            case SET:
            case ADD:
            case REMOVE:
            case REMOVE_ALL:
                return new Class[]{Player.class};
            default:
                return new Class[0];
        }
    }

    @Override
    public void change( Event e, Object[] delta, Changer.ChangeMode mode) {
		String currentGame = scope ? lastGame.getName() : gameName.getSingle(e);
        Gami.getGameByName(currentGame).ifPresent(gameFound -> {
            for (Object obj : delta) {
                if (obj instanceof Player) {
                    Player player = (Player) obj;
                    switch (mode) {
                        case ADD:
							if (gameFound.getMaxPlayer() > gameFound.getPlayers().size()) {
                                if (!gameFound.hasPlayer(player)) {
                                    gameFound.addPlayer(player);
                                }
                            }
                            break;
                        case REMOVE:
                            gameFound.removePlayer(player);
                            break;
                        case REMOVE_ALL:
                            for (Object playerObject : gameFound.getPlayers()) {
                                Player p = (Player) playerObject;
                                gameFound.removePlayer(p);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Game> getReturnType() {
        return Game.class;
    }

    @Override
    public String toString( Event e, boolean debug ) {
        return "game " + gameName.toString(e, debug);
    }
}
