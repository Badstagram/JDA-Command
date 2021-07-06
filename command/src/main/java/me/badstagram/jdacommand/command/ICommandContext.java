package me.badstagram.jdacommand.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public interface ICommandContext {
    List<String> getArgs();

    Guild getGuild();

    Member getMember();

    User getAuthor();

    JDA getJDA();

    Message getMessage();

    GuildMessageReceivedEvent getEvent();

    TextChannel getChannel();

    Member getSelfMember();

    SelfUser getSelfUser();

    void reply(String msg);

    void replyOrDefault(MessageEmbed embed, String msg);

    void replyPinging(String msg);

    CommandClient getClient();

}
