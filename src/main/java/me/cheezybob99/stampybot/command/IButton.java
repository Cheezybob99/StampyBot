package me.cheezybob99.stampybot.command;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface IButton {

    void onClick(String id, ButtonClickEvent event);

}
