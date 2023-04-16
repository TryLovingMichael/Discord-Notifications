import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class DiscordNotification extends JavaPlugin implements Listener {

    private String webhookUrl;

    private DiscordApi discordApi;

    private Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        // Load config.yml
        saveDefaultConfig();

        // Get the webhook URL from config.yml
        FileConfiguration config = getConfig();
        webhookUrl = config.getString("webhook_url");

      
      //You can add it so you can change your bot token in config, if you would like.
        // Initialize the Discord API
        discordApi = new DiscordApiBuilder().setToken("YOUR_DISCORD_BOT_TOKEN").login().join();

        // Register the listener
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        logger.log(Level.INFO, "DiscordNotification plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Disconnect the Discord API when the server shuts down
        discordApi.disconnect();
        logger.log(Level.INFO, "DiscordNotification plugin disabled.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get the player's name and join message
        String playerName = event.getPlayer().getName();
        String joinMessage = event.getJoinMessage();

        // Send a Discord message with the player's name and join message
        TextChannel channel = discordApi.getTextChannelById("YOUR_DISCORD_CHANNEL_ID").orElse(null);
        if (channel != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(playerName + " joined the server")
                .setDescription(joinMessage)
                .setColor(0x00ff00);
            MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build())
                .setAvatarUrl("https://example.com/avatar.png")
                .setUsername("Minecraft Server");
            channel.sendMessage(messageBuilder);
        }
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public DiscordApi getDiscordApi() {
        return discordApi;
    }

}
