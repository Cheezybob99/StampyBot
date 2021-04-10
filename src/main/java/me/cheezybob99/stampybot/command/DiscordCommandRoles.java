package me.cheezybob99.stampybot.command;

import me.cheezybob99.stampybot.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction.CommandData;

import java.util.HashMap;

public class DiscordCommandRoles extends DiscordCommand {

    public DiscordCommandRoles(Main main) {

        super(main, "roles", main.getConfig().getAdminRole());

    }

    @Override
    public void onCommand(SlashCommandEvent event) {

        Member member = event.getMember();
        Guild guild = main.getJda().getGuildById(main.getConfig().getGuild());

        if (guild == null || member == null) {
            event.reply("An error has occurred!").setEphemeral(true).queue();
            return;
        }

        HashMap<Role, Integer> roleAmount = new HashMap<>();

        for (Role role : guild.getRoles()) {
            roleAmount.put(role, guild.getMembersWithRoles(role).size());
        }

        StringBuilder sb = new StringBuilder();
        roleAmount.forEach((r, i) -> {
            if (r.isPublicRole()) {
                sb.append("everyone").append(": ").append(guild.getMembers().size()).append("\n");
            }
            else {
                sb.append(r.getName()).append(": ").append(i).append("\n");
            }
        });

        String message = sb.toString();
        event.reply(message).queue();

    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("roles", "View the role stats.");
    }

}
