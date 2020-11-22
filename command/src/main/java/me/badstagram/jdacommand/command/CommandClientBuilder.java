package me.badstagram.jdacommand.command;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.util.*;

public class CommandClientBuilder {
    private String ownerId = "";
    private String[] coOwnerIds = new String[0];
    private String prefix = "!";
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandClientBuilder setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public CommandClientBuilder setCoOwnerIds(String[] coOwnerIds) {
        this.coOwnerIds = coOwnerIds;
        return this;
    }

    public CommandClientBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandClientBuilder addCommand(Command cmd) {
        String name = cmd.name;

        if (this.commands.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Command %s is already registered", name));
        }
        this.commands.put(name, cmd);
        return this;
    }

    public CommandClientBuilder addCommands(Command... cmds) {

        for (Command cmd : cmds) {
            this.addCommand(cmd);
        }
        return this;
    }

    public CommandClient build() {
        return new CommandClient(ownerId, coOwnerIds, prefix, commands);
    }
}
