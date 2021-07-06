package me.badstagram.jdacommand.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandClient extends ListenerAdapter {
    private final String ownerId;
    private final String[] coOwnerIds;
    private final String prefix;
    private final HashMap<String, Command> commands;
    protected final Map<String, OffsetDateTime> cooldowns = new HashMap<>();


    public CommandClient(String ownerId, String[] coOwnerIds, String prefix, HashMap<String, Command> commands) {

        this.ownerId = ownerId;
        this.coOwnerIds = coOwnerIds;
        this.prefix = prefix;
        this.commands = commands;

        Checks.check(ownerId != null, "Owner ID was set null or not set! Please provide an User ID to register as the owner!");
        Checks.isSnowflake(this.ownerId);
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public HashMap<String, Command> getCommands() {
        return this.commands;
    }

    // handle command
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        // ignore bots
        if (event.getAuthor().isBot()) {
            return;
        }

        String rawContent = event.getMessage().getContentRaw();

        String[] parts = null;
        String prefix = this.prefix;

        if (rawContent.toLowerCase().startsWith(prefix.toLowerCase()))
            parts = splitOnPrefix(rawContent, prefix.length());

        // if command doesnt start with a valid prefix, we can't use it so return
        if (parts == null) {
            return;
        }

        String name = parts[0];
        String args = parts[1] == null ? "" : parts[1];
        final Command command = this.commands.getOrDefault(name, null); // this will be null if it's not a command

        // message wasn't a command, so return
        if (command == null) {
            return;
        }

        CommandContext ctx = new CommandContext(event, args, this);

        command.run(ctx);


    }

    private String[] splitOnPrefix(String content, int len) {
        return Arrays.copyOf(content.substring(len).trim().split("\\s+", 2), 2);
    }

    public int getRemainingCooldown(String name)
    {
        if(cooldowns.containsKey(name))
        {
            int time = (int) Math.ceil(OffsetDateTime.now().until(cooldowns.get(name), ChronoUnit.MILLIS) / 1000D);
            if(time<=0)
            {
                cooldowns.remove(name);
                return 0;
            }
            return time;
        }
        return 0;
    }


    public void applyCooldown(String name, int seconds)
    {
        cooldowns.put(name, OffsetDateTime.now().plusSeconds(seconds));
    }


    public void cleanCooldowns()
    {
        OffsetDateTime now = OffsetDateTime.now();
        cooldowns.keySet().stream().filter((str) -> (cooldowns.get(str).isBefore(now)))
                .collect(Collectors.toList()).forEach(cooldowns::remove);
    }

}
