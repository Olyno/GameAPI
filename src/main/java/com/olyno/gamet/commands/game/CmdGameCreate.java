package com.olyno.gamet.commands.game;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.olyno.gamet.util.commands.GameCommand;
import com.olyno.gami.Gami;
import com.olyno.gami.models.Game;

public class CmdGameCreate extends GameCommand {

    public CmdGameCreate() {
        super();
        this.setDescription("Create a new game.");
        this.setPattern("create (\\w+)");
        this.setUsage("create <game name>");
        this.addPermission("game.create");
    }

    @Override
    public void execute(CommandSender sender, ArrayList<String> args) {
        String gameName = args.get(0).toLowerCase();
        if (Gami.getGameByName(gameName).isEmpty()) {
            new Game(gameName);
        }
    }
    
}