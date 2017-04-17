package fr.horgeon.bukkit.packetsutilities;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FakeTeam {
	private final String name;
	private String prefix;
	private String suffix;
	private Color color = Color.RESET;
	private List<String> players;
	private String currentPlayer;
	private String oldPlayer;
	private NameTagVisibility nameTagVisibility = NameTagVisibility.ALWAYS;
	private CollisionRule collisionRule = CollisionRule.ALWAYS;

	private static final byte FLAGS_FRIENDLY_FIRE = 0x1;
	private static final byte FLAGS_SEE_OWN_TEAM_INVISIBLE_PLAYERS = 0x2;

	private byte packOptionData = 0;
	private boolean friendlyFire = false;
	private boolean seeOwnTeamInvisiblePlayers = false;

	public enum NameTagVisibility {
		ALWAYS( "always" ),
		HIDE_FROM_OTHER_TEAMS( "hideForOtherTeams" ),
		HIDE_FROM_OWN_TEAM( "hideForOwnTeam" ),
		NEVER( "never" );

		final String string;

		NameTagVisibility( String value ) {
			string = value;
		}
	}

	public enum CollisionRule {
		ALWAYS( "always" ),
		PUSH_OTHER_TEAMS( "pushOtherTeams" ),
		PUSH_OWN_TEAM( "pushOwnTeam" ),
		NEVER( "never" );

		final String string;

		CollisionRule( String value ) {
			string = value;
		}
	}

	public enum Color {
		BLACK( (byte) 0, ChatColor.BLACK ),
		DARK_BLUE( (byte) 1, ChatColor.DARK_BLUE ),
		DARK_GREEN( (byte) 2, ChatColor.DARK_GREEN ),
		DARK_AQUA( (byte) 3, ChatColor.DARK_AQUA ),
		DARK_RED( (byte) 4, ChatColor.DARK_RED ),
		DARK_PURPLE( (byte) 5, ChatColor.DARK_PURPLE ),
		GOLD( (byte) 6, ChatColor.GOLD ),
		GRAY( (byte) 7, ChatColor.GRAY ),
		DARK_GRAY( (byte) 8, ChatColor.DARK_GRAY ),
		BLUE( (byte) 9, ChatColor.BLUE ),
		GREEN( (byte) 10, ChatColor.GREEN ),
		AQUA( (byte) 11, ChatColor.AQUA ),
		RED( (byte) 12, ChatColor.RED ),
		LIGHT_PURPLE( (byte) 13, ChatColor.LIGHT_PURPLE ),
		YELLOW( (byte) 14, ChatColor.YELLOW ),
		WHITE( (byte) 15, ChatColor.WHITE ),
		RESET( (byte) -1, ChatColor.RESET );

		final byte code;
		final ChatColor equivalent;

		Color( byte code, ChatColor equivalent ) {
			this.code = code;
			this.equivalent = equivalent;
		}
	}

	private boolean prefixChanged, suffixChanged, colorChanged = false, playerChanged = false;
	private boolean first = true;

	public FakeTeam( String name, String prefix, String suffix ) {
		this.name = name.substring( 0, 16 );
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public FakeTeam( String name ) {
		this( name, "", "" );
	}

	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix( String prefix ) {
		if( this.prefix == null || !this.prefix.equals( prefix ) )
			this.prefixChanged = true;
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix( String suffix ) {
		if( this.suffix == null || !this.suffix.equals( prefix ) )
			this.suffixChanged = true;
		this.suffix = suffix;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor( Color color ) {
		this.color = color;
	}

	public List<String> getPlayers() {
		return this.players;
	}

	public void setPlayers( List<String> players ) {
		this.players = players;
	}

	public NameTagVisibility getNameTagVisibility() {
		return this.nameTagVisibility;
	}

	public void setNameTagVisibility( NameTagVisibility nameTagVisibility ) {
		this.nameTagVisibility = nameTagVisibility;
	}

	public CollisionRule getCollisionRule() {
		return this.collisionRule;
	}

	public void setCollisionRule( CollisionRule collisionRule ) {
		this.collisionRule = collisionRule;
	}

	public boolean getFriendlyFire() {
		return this.friendlyFire;
	}

	public void setFriendlyFire( boolean friendlyFire ) {
		this.friendlyFire = friendlyFire;
		setPackOptionData();
	}

	public boolean getSeeOwnTeamInvisiblePlayers() {
		return this.seeOwnTeamInvisiblePlayers;
	}

	public void setSeeOwnTeamInvisiblePlayers( boolean seeOwnTeamInvisiblePlayers ) {
		this.seeOwnTeamInvisiblePlayers = seeOwnTeamInvisiblePlayers;
		setPackOptionData();
	}

	protected void setPackOptionData() {
		this.packOptionData = (byte) ( ( friendlyFire ? FLAGS_FRIENDLY_FIRE : (byte) 0 ) | ( seeOwnTeamInvisiblePlayers ? FLAGS_SEE_OWN_TEAM_INVISIBLE_PLAYERS : (byte) 0  ) );
	}

	private WrapperPlayServerScoreboardTeam createPacket( int mode ) {
		WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
		packet.setName( name );
		packet.setMode( mode );
		packet.setDisplayName( "" );
		packet.setPrefix( prefix );
		packet.setSuffix( suffix );
		packet.setColor( color.code );
		packet.setPackOptionData( packOptionData );
		packet.setNameTagVisibility( nameTagVisibility.string );
		packet.setCollisionRule( collisionRule.string );
		if( this.players != null )
			packet.setPlayers( players );

		return packet;
	}

	public void createTeam( Player player ) {
		createPacket( WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED ).sendPacket( player );
	}

	public void updateTeam( Player player ) {
		createPacket( WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED ).sendPacket( player );
	}

	public void removeTeam( Player player ) {
		WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
		packet.setMode( WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED );
		packet.setName( name );

		first = true;
		packet.sendPacket( player );
	}

	public void setPlayer( String name ) {
		if( this.currentPlayer == null || !this.currentPlayer.equals( name ) )
			this.playerChanged = true;
		this.oldPlayer = this.currentPlayer;
		this.currentPlayer = name;
	}

	public void sendLine( Player player ) {
		if( first ) {
			createTeam( player );
		} else if( prefixChanged || suffixChanged || colorChanged ) {
			updateTeam( player );
		}

		if( first || playerChanged ) {
			if( oldPlayer != null )
				addOrRemovePlayer( player, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED, oldPlayer );
			changePlayer( player );
		}

		if( first )
			first = false;
	}

	public void reset() {
		prefixChanged = false;
		suffixChanged = false;
		colorChanged = false;
		playerChanged = false;
		oldPlayer = null;
	}

	public void changePlayer( Player player ) {
		addPlayer( player, currentPlayer );
	}

	public void addPlayer( Player player, String playerName ) {
		addOrRemovePlayer( player, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED, playerName );
	}

	public void addPlayers( Player player, List<String> players ) {
		addOrRemovePlayers( player, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED, players );
	}

	public void removePlayer( Player player, String playerName ) {
		addOrRemovePlayer( player, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED, playerName );
	}

	public void removePlayers( Player player, List<String> players ) {
		addOrRemovePlayers( player, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED, players );
	}

	private void addOrRemovePlayer( Player player, int mode, String playerName ) {
		List<String> players = new ArrayList<>();
		players.add( playerName );
		addOrRemovePlayers( player, mode, players );
	}

	private void addOrRemovePlayers( Player player, int mode, List<String> players ) {
		WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
		packet.setName( name );
		packet.setMode( mode );

		packet.setPlayers( players );
		packet.sendPacket( player );
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public String getValue() {
		return getPrefix() + getCurrentPlayer() + getSuffix();
	}

	public void setValue( String value ) {
		if( value.length() <= 16 ) {
			setPrefix( "" );
			setSuffix( "" );
			setPlayer( value );
		} else if( value.length() <= 32 ) {
			setPrefix( value.substring( 0, 16 ) );
			setPlayer( value.substring( 16 ) );
			setSuffix( "" );
		} else if( value.length() <= 48 ) {
			setPrefix( value.substring( 0, 16 ) );
			setPlayer( value.substring( 16, 32 ) );
			setSuffix( value.substring( 32 ) );
		} else {
			throw new IllegalArgumentException( "Too long value ! Max 48 characters, value was " + value.length() + " !" );
		}
	}
}