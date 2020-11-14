import net.dv8tion.jda.api.Permission;

import java.util.List;

public interface ICommand {
    /**
     * Executes a command
     *
     * @param ctx The context of the command
     */
    void dispatchCommand(CommandContext ctx);

    /**
     * Get the command name
     *
     * @return The command name
     */
    String getName();

    /**
     * Gets the command help
     *
     * @return The command help
     */
    String getHelp();

    /**
     * Get the permissions required by the user to run the command
     *
     * @return the permissions required by the user to run the command
     */
    List<Permission> getUserPermissions();

    /**
     * Get the permissions required by the bot to run the command
     *
     * @return the permissions required by the bot to run the command
     */
    List<Permission> getBotPermissions();

    /**
     * if the command should be locked to the bot owner(s)
     *
     * @return if the command should be locked to the bot owner(s)
     */
    default boolean isCommandRestricted() {
        return false;
    }

    /**
     * if the command should be hidden from help
     *
     * @return if the command should be hidden from help
     */
    default boolean isCommandHidden() {
        return false;
    }

    /**
     * if the command can only be ran in NSFW channels
     *
     * @return if the command can only be ran in NSFW channels
     */
    default boolean isNSFW() {
        return false;
    }


}
