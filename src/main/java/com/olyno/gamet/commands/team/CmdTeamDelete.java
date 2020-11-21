package com.olyno.gamet.commands.team;

import java.util.ArrayList;

import com.olyno.gamet.util.commands.GameCommand;
import com.olyno.gami.Gami;

import org.bukkit.command.CommandSender;

public class CmdTeamDelete extends GameCommand {

    public CmdTeamDelete() {
        super();
        this.setDescription("Delete an existing team in a game.");
        this.setPattern("delete (\\w+) (in|of|from) (\\w+)");
        this.setUsage("delete <team name> in <game name>");
        this.addPermission("team.delete");
    }

    @Override
    public void execute(CommandSender sender, ArrayList<String> args) {
        String gameName = args.get(2).toLowerCase();
        String teamName = args.get(0).toLowerCase();
        if (Gami.getGames().containsKey(gameName)) {
            Gami.getGames().get(gameName).getTeams().remove(teamName);
        }
    }
    
}