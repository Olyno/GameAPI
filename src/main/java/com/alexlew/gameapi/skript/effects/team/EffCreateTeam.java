package com.alexlew.gameapi.skript.effects.team;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.alexlew.gameapi.GameAPI;
import com.alexlew.gameapi.types.Game;
import com.alexlew.gameapi.types.Team;
import org.bukkit.event.Event;

@Name("Create Team for game")
@Description("Create a team for a game")
@Examples({
        "command create <text>:",
        "\ttrigger:",
        "\t\tcreate team \"red\" for game \"Test\""
})
@Since("1.0")

public class EffCreateTeam extends Effect {

    public static Team lastCreatedTeam;

    static {
        Skript.registerEffect(EffCreateTeam.class,
                "create [(the|a)] team %string% (for|of|from|in) %game%"
        );
    }

    private Expression<String> team;
    private Expression<Game> game;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init( Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult ) {
        team = (Expression<String>) expr[0];
        game = (Expression<Game>) expr[1];
        return true;
    }

    @Override
    protected void execute( Event e ) {
        String teamName = team.getSingle(e);
        Game mg = game.getSingle(e);
        if (teamName != null && mg != null) {
            if (!teamName.replaceAll(" ", "").equals("")) {
                if (mg.teamExists(team.getSingle(e))) {
                    GameAPI.error("The team " + teamName + " already exist in the game " + mg.getName());
                } else {
                    mg.addTeam(new Team(team.getSingle(e)));
                    lastCreatedTeam = mg.getTeam(team.getSingle(e));
                }
            } else {
                GameAPI.error("A team can't have a empty name (Current name: " + team.getSingle(e) + ")");
            }
        } else {
            GameAPI.error("A team can't be null (Current name: " + team.getSingle(e) + ")");
        }

    }

    @Override
    public String toString( Event e, boolean debug ) {
        return "create the game \"" + game.getSingle(e) + "\"";
    }

}