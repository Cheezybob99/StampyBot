package me.cheezybob99.stampybot.command;

import me.cheezybob99.stampybot.Main;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction.CommandData;

public abstract class DiscordCommand {

    protected final Main main;
    private final String name;
    private final String roleID;

    public DiscordCommand(Main main, String name, String roleID) {

        this.main = main;
        this.name = name;
        this.roleID = roleID;

    }

    public abstract void onCommand(SlashCommandEvent event);
    public abstract CommandData buildCommand();

    public String getName() {
        return name;
    }

    public String getRoleID() {
        return roleID;
    }

}
