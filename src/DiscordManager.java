import enums.DiscordExceptionType;
import exceptions.DiscordException;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.List;

public class DiscordManager {
    //  Singleton
    private DiscordManager() {
    }

    public static DiscordManager getInstance() {
        return DiscordManagerHolder.INSTANCE;
    }

    public static class DiscordManagerHolder {
        public static final DiscordManager INSTANCE = new DiscordManager();
    }

    //  Fields
    private IDiscordClient discordClient;
    private IGuild guild;
    private IVoiceChannel voiceChannel;

    //  Getters
    public IDiscordClient getDiscordClient() {
        return discordClient;
    }

    public IGuild getGuild() {
        return guild;
    }

    public List<IGuild> getGuildList() {
        return discordClient.getGuilds();
    }

    public IVoiceChannel getVoiceChannel() {
        return voiceChannel;
    }

    public boolean isClientReady() {
        return discordClient.isReady();
    }

    //  Setters
    @SuppressWarnings("WeakerAccess")
    public void setDiscordClient(IDiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @SuppressWarnings("WeakerAccess")
    public void setVoiceChannel(IVoiceChannel channel) {
        this.voiceChannel = channel;
    }

    @SuppressWarnings("WeakerAccess")
    public void setGuild(IGuild guild) {
        this.guild = guild;
    }

    //  Methods
    public void joinChannel(int guildIndex, int channelIndex) {
        setGuild(getGuildList().get(guildIndex));
        setVoiceChannel(guild.getVoiceChannels().get(channelIndex));
        getVoiceChannel().join();
        SoundManager.getInstance().createDiscordSoundPlayer(getVoiceChannel().getGuild());
    }

    public void leaveChannel() {
        getVoiceChannel().leave();
        setGuild(null);
        setVoiceChannel(null);
        SoundManager.getInstance().clearDiscordPlayer();
    }

    public void logIn(String token) throws DiscordException {
        ClientBuilder discordBuilder = new ClientBuilder();
        discordBuilder.withToken(token);
        try {
            discordClient = discordBuilder.login();
        } catch (sx.blah.discord.util.DiscordException e) {
            Logger.getInstance().log(e.getErrorMessage());
            if (e.getErrorMessage().contains("Unauthorized")) {
                throw new DiscordException(DiscordExceptionType.UNAUTHORIZED);
            }
        }
    }

    public void logOut() {
        getDiscordClient().logout();
        setDiscordClient(null);
    }


}