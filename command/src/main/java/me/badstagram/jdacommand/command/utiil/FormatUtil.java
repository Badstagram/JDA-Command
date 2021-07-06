package me.badstagram.jdacommand.command.utiil;

public class FormatUtil {
    /**
     * Formats seconds into a human readable time
     * Code adapted from https://github.com/jagrosh/Vortex/blob/7dfaf21b33ce0302e485ce61e0d4b8572a2c4cfe/src/main/java/com/jagrosh/vortex/utils/FormatUtil.java#L211
     *
     * @param timeSeconds The time in seconds
     * @return The human readable time
     * @author John Grosh (jagrosh)
     */
    public static String secondsToTime(long timeSeconds) {
        StringBuilder builder = new StringBuilder();
        int years = (int) (timeSeconds / (60 * 60 * 24 * 365));
        if (years > 0) {
            builder.append(years).append(" years ");
            timeSeconds = timeSeconds % (60 * 60 * 24 * 365);
        }
        int weeks = (int) (timeSeconds / (60 * 60 * 24 * 7));
        if (weeks > 0) {
            builder.append(weeks).append(" weeks ");

            timeSeconds = timeSeconds % (60 * 60 * 24 * 7);
        }
        int days = (int) (timeSeconds / (60 * 60 * 24));
        if (days > 0) {
            builder.append(days).append(" days ");
            timeSeconds = timeSeconds % (60 * 60 * 24);
        }
        int hours = (int) (timeSeconds / (60 * 60));
        if (hours > 0) {
            builder.append(hours).append(" hours ");
            timeSeconds = timeSeconds % (60 * 60);
        }
        int minutes = (int) (timeSeconds / (60));
        if (minutes > 0) {
            builder.append(minutes).append(" minutes ");
            timeSeconds = timeSeconds % (60);
        }
        if (timeSeconds > 0)
            builder.append(timeSeconds).append(" seconds");
        String str = builder.toString();
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2);
        if (str.isEmpty())
            str = "No time";
        return str;
    }

}
