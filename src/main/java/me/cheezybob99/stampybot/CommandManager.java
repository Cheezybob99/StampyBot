package me.cheezybob99.stampybot;

import me.cheezybob99.stampybot.command.DiscordCommand;
import me.cheezybob99.stampybot.command.DiscordCommandAddAlertWord;
import me.cheezybob99.stampybot.command.DiscordCommandRoles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;

import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {

    private final Main main;
    private final ArrayList<DiscordCommand> commands;

    public CommandManager(Main main) {

        this.main = main;
        this.commands = new ArrayList<>();
        registerCommands();

    }

    private void registerCommands() {

        commands.add(new DiscordCommandRoles(main));
        commands.add(new DiscordCommandAddAlertWord(main));

        CommandUpdateAction commandUpdateAction = main.getJda().getGuildById(main.getConfig().getGuild()).updateCommands();
        for (DiscordCommand discordCommand : commands) {
            commandUpdateAction.addCommands(discordCommand.buildCommand());
        }
        commandUpdateAction.queue();

    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if (event.getGuild() == null || !event.getGuild().getId().equals(main.getConfig().getGuild())) {
            return;
        }

        DiscordCommand discordCommand = null;

        for (DiscordCommand d : commands) {
            if (d.getName().equalsIgnoreCase(event.getName())) {
                discordCommand = d;
            }
        }

        if (discordCommand == null || event.getMember() == null) {
            return;
        }


        if (discordCommand.getRoleID() != null && !hasRole(event.getMember(), discordCommand.getRoleID())) {
            event.reply("Invalid permissions!").setEphemeral(true).queue();
            return;
        }

        discordCommand.onCommand(event);

    }

    private boolean hasRole(Member member, String roleID) {
        return member.getRoles().stream().filter(r -> r.getId().equals(roleID)).findAny().orElse(null) != null;
    }

}
