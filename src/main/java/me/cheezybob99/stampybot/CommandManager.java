package me.cheezybob99.stampybot;

import me.cheezybob99.stampybot.command.DiscordCommand;
import me.cheezybob99.stampybot.command.DiscordCommandAddAlertWord;
import me.cheezybob99.stampybot.command.DiscordCommandRoles;
import me.cheezybob99.stampybot.command.IButton;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandManager extends ListenerAdapter {

    private final Main main;
    private final ArrayList<DiscordCommand> commands;
    private final HashMap<String, IButton> buttons;

    public CommandManager(Main main) {

        this.main = main;
        this.commands = new ArrayList<>();
        this.buttons = new HashMap<>();
        registerCommands();

    }

    private void registerCommands() {

        commands.add(new DiscordCommandRoles(main));
        commands.add(new DiscordCommandAddAlertWord(main));

        CommandListUpdateAction commandUpdateAction = main.getJda().getGuildById(main.getConfig().getGuild()).updateCommands();
        for (DiscordCommand discordCommand : commands) {
            commandUpdateAction.addCommands(discordCommand.buildCommand());
        }
        commandUpdateAction.queue();

    }

    public void registerButtons(String id, IButton button) {
        buttons.put(id, button);
    }

    public void unregisterButton(String id) {
        buttons.remove(id);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {

        IButton button = buttons.get(event.getButton().getId());

        if (button != null) {
            button.onClick(event.getButton().getId(), event);
        }

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
