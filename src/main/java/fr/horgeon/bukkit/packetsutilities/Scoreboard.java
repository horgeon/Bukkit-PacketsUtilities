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
	private final FakeTeam[] lines = new FakeTeam[ 15 ];
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

		for( FakeTeam team : lines )
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
		FakeTeam team = getOrCreateTeam( line );
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
		FakeTeam team = getOrCreateTeam( line );
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
	 * @return the {@link FakeTeam} used to display this line
	 */
	public FakeTeam getTeam( int line ) {
		if( line > 14 || line < 0 )
			return null;
		return getOrCreateTeam( line );
	}

	private void sendLine( int line ) {
		if( line > 14 || line < 0 || !created )
			return;

		int score = 15 - line;
		FakeTeam val = getOrCreateTeam( line );
		val.sendLine( player );

		sendScore( val.getCurrentPlayer(), score );
		val.reset();
	}

	private FakeTeam getOrCreateTeam( int line ) {
		if( lines[ line ] == null )
			lines[ line ] = new FakeTeam( "__fakeScore" + line );

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
}