package fr.horgeon.bukkit.packetsutilities;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardDisplayObjective;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardObjective;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardScore;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyuiop
 * @author Horgeon
 */
public class Scoreboard {
	private boolean created = false;
	private final VirtualTeam[] lines = new VirtualTeam[ 15 ];
	private final Player player;
	private String objectiveName, objectiveDisplayName;

	/**
	 * Create a scoreboard sign for a given player and using a specifig objective name
	 *
	 * @param player        the player viewing the scoreboard sign
	 * @param objectiveName the name of the scoreboard sign (displayed at the top of the scoreboard)
	 */
	public Scoreboard( Player player, String objectiveName ) {
		this( player, objectiveName, objectiveName );
	}

	/**
	 * Create a scoreboard sign for a given player and using a specifig objective name
	 *
	 * @param player               the player viewing the scoreboard sign
	 * @param objectiveName        the name of the scoreboard sign
	 * @param objectiveDisplayName the display name of the scoreboard sign (displayed at the top of the scoreboard)
	 */
	public Scoreboard( Player player, String objectiveName, String objectiveDisplayName ) {
		this.player = player;
		this.objectiveName = objectiveName;
		this.objectiveDisplayName = objectiveDisplayName;
	}

	/**
	 * Send the initial creation packets for this scoreboard sign. Must be called at least once.
	 */
	public void create() {
		if( created ) return;

		WrapperPlayServerScoreboardObjective packet = new WrapperPlayServerScoreboardObjective();
		packet.setMode( WrapperPlayServerScoreboardObjective.Mode.ADD_OBJECTIVE );
		packet.setName( this.objectiveName );
		packet.setDisplayName( this.objectiveDisplayName );
		packet.sendPacket( player );

		WrapperPlayServerScoreboardDisplayObjective dispacket = new WrapperPlayServerScoreboardDisplayObjective();
		dispacket.setPosition( WrapperPlayServerScoreboardDisplayObjective.Position.SIDEBAR );
		dispacket.setScoreName( this.objectiveName );
		dispacket.sendPacket( player );

		for( int i = 0; i < lines.length; i++ ) {
			sendLine( i );
		}

		created = true;
	}

	/**
	 * Send the packets to remove this scoreboard sign. A destroyed scoreboard sign must be recreated using {@link Scoreboard#create()} in order
	 * to be used again
	 */
	public void destroy() {
		if( !created )
			return;

		WrapperPlayServerScoreboardObjective packet = new WrapperPlayServerScoreboardObjective();
		packet.setMode( WrapperPlayServerScoreboardObjective.Mode.REMOVE_OBJECTIVE );
		packet.setName( this.objectiveName );
		packet.sendPacket( player );

		for( VirtualTeam team : lines )
			if( team != null )
				team.removeTeam( player );

		created = false;
	}

	/**
	 * Change the name of the objective. The name is displayed at the top of the scoreboard.
	 *
	 * @param name the name of the objective, max 32 char
	 */
	public void setObjectiveName( String name ) {
		this.objectiveName = name;

		if( created ) {
			WrapperPlayServerScoreboardObjective packet = new WrapperPlayServerScoreboardObjective();
			packet.setMode( WrapperPlayServerScoreboardObjective.Mode.UPDATE_VALUE );
			packet.setName( this.objectiveName );
			packet.setDisplayName( name );
			packet.sendPacket( player );
		}
	}

	/**
	 * Change a scoreboard line and send the packets to the player. Can be called async.
	 *
	 * @param line  the number of the line (0 <= line < 15)
	 * @param value the new value for the scoreboard line
	 */
	public void setLine( int line, String value ) {
		VirtualTeam team = getOrCreateTeam( line );
		String old = team.getCurrentPlayer();

		if( old != null && created )
			removeLine( old );

		team.setValue( value );
		sendLine( line );
	}

	/**
	 * Remove a given scoreboard line
	 *
	 * @param line the line to remove
	 */
	public void removeLine( int line ) {
		VirtualTeam team = getOrCreateTeam( line );
		String old = team.getCurrentPlayer();

		if( old != null && created ) {
			removeLine( old );
			team.removeTeam( player );
		}

		lines[ line ] = null;
	}

	/**
	 * Get the current value for a line
	 *
	 * @param line the line
	 * @return the content of the line
	 */
	public String getLine( int line ) {
		if( line > 14 || line < 0 )
			return null;
		return getOrCreateTeam( line ).getValue();
	}

	/**
	 * Get the team assigned to a line
	 *
	 * @return the {@link VirtualTeam} used to display this line
	 */
	public VirtualTeam getTeam( int line ) {
		if( line > 14 || line < 0 )
			return null;
		return getOrCreateTeam( line );
	}

	private void sendLine( int line ) {
		if( line > 14 || line < 0 || !created )
			return;

		int score = 15 - line;
		VirtualTeam val = getOrCreateTeam( line );
		val.sendLine( player );

		sendScore( val.getCurrentPlayer(), score );
		val.reset();
	}

	private VirtualTeam getOrCreateTeam( int line ) {
		if( lines[ line ] == null )
			lines[ line ] = new VirtualTeam( "__fakeScore" + line );

		return lines[ line ];
	}

	/*
		Factories
		 */
	private void sendScore( String line, int score ) {
		WrapperPlayServerScoreboardScore packet = new WrapperPlayServerScoreboardScore();
		packet.setScoreboardAction( EnumWrappers.ScoreboardAction.CHANGE );
		packet.setObjectiveName( this.objectiveName );
		packet.setScoreName( line );
		packet.setValue( score );
		packet.sendPacket( player );
	}

	private void removeLine( String line ) {
		WrapperPlayServerScoreboardScore packet = new WrapperPlayServerScoreboardScore();
		packet.setScoreName( line );
		packet.setObjectiveName( this.objectiveName );
		packet.setScoreboardAction( EnumWrappers.ScoreboardAction.REMOVE );
		packet.sendPacket( player );
	}

	/**
	 * This class is used to manage the content of a line. Advanced users can use it as they want, but they are encouraged to read and understand the
	 * code before doing so. Use these methods at your own risk.
	 */
	public class VirtualTeam {
		private final String name;
		private String prefix;
		private String suffix;
		private String currentPlayer;
		private String oldPlayer;

		private boolean prefixChanged, suffixChanged, playerChanged = false;
		private boolean first = true;

		private VirtualTeam( String name, String prefix, String suffix ) {
			this.name = name;
			this.prefix = prefix;
			this.suffix = suffix;
		}

		private VirtualTeam( String name ) {
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

		private WrapperPlayServerScoreboardTeam createPacket( int mode ) {
			WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
			packet.setName( name );
			packet.setMode( mode );
			packet.setDisplayName( "" );
			packet.setPrefix( prefix );
			packet.setSuffix( suffix );
			packet.setColor( 0 );
			packet.setPackOptionData( 0 );
			packet.setNameTagVisibility( "always" );

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
			} else if( prefixChanged || suffixChanged ) {
				updateTeam( player );
			}

			if( first || playerChanged ) {
				if( oldPlayer != null )                                        // remove these two lines ?
					addOrRemovePlayer( player, 4, oldPlayer );    //
				changePlayer( player );
			}

			if( first )
				first = false;
		}

		public void reset() {
			prefixChanged = false;
			suffixChanged = false;
			playerChanged = false;
			oldPlayer = null;
		}

		public void changePlayer( Player player ) {
			addOrRemovePlayer( player, 3, currentPlayer );
		}

		public void addOrRemovePlayer( Player player, int mode, String playerName ) {
			WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
			packet.setName( name );
			packet.setMode( mode );

			List<String> players = new ArrayList<String>();
			players.add( playerName );
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
}