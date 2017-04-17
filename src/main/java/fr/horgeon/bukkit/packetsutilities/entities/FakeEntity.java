package fr.horgeon.bukkit.packetsutilities.entities;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Level;

public class FakeEntity {
	// Next entity ID
	protected static int NEXT_ID = 6000;
	protected static String PREFIX = "fakeentity";

	protected EntityType type;

	// Metadata flags
	protected static final byte METADATA_FLAGS_INVISIBLE = 0x20;
	protected static final byte METADATA_FLAGS_GLOWING = 0x40;

	// Metadata indices
	protected static final Map.Entry<Integer, Class> METADATA_FLAGS = new AbstractMap.SimpleImmutableEntry( 0, Byte.class );
	protected static final Map.Entry<Integer, Class> METADATA_NAME = new AbstractMap.SimpleImmutableEntry( 0, String.class );
	protected static final Map.Entry<Integer, Class> METADATA_SHOW_NAME = new AbstractMap.SimpleImmutableEntry( 3, Boolean.class );
	protected static final Map.Entry<Integer, Class> METADATA_SILENT = new AbstractMap.SimpleImmutableEntry( 4, Boolean.class );
	//protected static final Map.Entry<Integer, Class> METADATA_NO_GRAVITY = new AbstractMap.SimpleImmutableEntry( 5, Boolean.class );

	// Unique ID
	protected int id;
	protected UUID uuid;

	protected boolean visible = false;
	protected boolean glowing = false;
	protected boolean silent = false;
	protected boolean gravity = false;

	protected HashSet<Player> players = new HashSet<>();
	protected HashMap<String, Boolean> createds = new HashMap<>();

	protected Location location;
	protected ProtocolManager manager;

	public FakeEntity( EntityType type, Location location, ProtocolManager manager ) {
		this.type = type;
		this.location = location;
		this.manager = manager;
		this.id = NEXT_ID++;
		this.uuid = generateUUID( PREFIX, id );
	}

	public FakeEntity( EntityType type, Location location, ProtocolManager manager, UUID uuid ) {
		this.type = type;
		this.location = location;
		this.manager = manager;
		this.id = NEXT_ID++;
		this.uuid = uuid;
	}

	public FakeEntity( EntityType type, Location location, ProtocolManager manager, int id, UUID uuid ) {
		this.type = type;
		this.location = location;
		this.manager = manager;
		this.id = id;
		this.uuid = uuid;
	}

	public void addPlayer( Player player ) {
		this.players.add( player );
	}

	public void removePlayer( Player player ) {
		this.players.remove( player );
	}

	public static UUID generateUUID( String prefix, int id ) {
		SecureRandom random = new SecureRandom();
		StringBuilder uuidSb = new StringBuilder( prefix );
		uuidSb.append( id );
		int size = uuidSb.length();
		if( size > 32 )
			uuidSb.setLength( 32 );
		byte[] bytes = new byte[ 32 - size ];
		random.nextBytes( bytes );
		uuidSb.append( new String( bytes ) );

		return UUID.nameUUIDFromBytes( uuidSb.toString().getBytes() );
	}

	public void setVisible( boolean visible ) {
		// Make visible or invisible
		this.visible = visible;
		setMetadataFlags();
	}

	public void setGlowing( boolean glowing ) {
		// Make glowing or not
		this.glowing = glowing;
		setMetadataFlags();
	}

	public void setSilent( boolean silent ) {
		// Make silent or not
		this.silent = silent;
		setMetadata( METADATA_SILENT, silent );
	}

	/*public void setGravity( boolean gravity ) {
		// Make having gravity or not
		this.gravity = gravity;
		setMetadata( METADATA_NO_GRAVITY, !gravity );
	}*/

	protected void setMetadataFlags() {
		byte flags = (byte) ( ( visible ? (byte) 0 : METADATA_FLAGS_INVISIBLE ) | ( glowing ? METADATA_FLAGS_GLOWING : (byte) 0  ) );
		setMetadata( METADATA_FLAGS, flags );
	}

	protected void setMetadata( Map.Entry<Integer, Class> metadata, Object value ) {
		if( createds.size() > 0 ) {
			WrappedDataWatcher watcher = prepareMetadata( metadata, value );
			WrapperPlayServerEntityMetadata update = prepareUpdateMetadata( watcher );

			for( Player player : players ) {
				if( createds.get( player.getName() ) )
					sendPacket( player, update.getHandle() );
			}
		}
	}

	protected WrappedDataWatcher prepareMetadata( Map.Entry<Integer, Class> metadata, Object value ) {
		return prepareMetadata( new WrappedDataWatcher(), metadata, value );
	}

	protected WrappedDataWatcher prepareMetadata( WrappedDataWatcher watcher, Map.Entry<Integer, Class> metadata, Object value ) {
		WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get( metadata.getValue() );
		WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject( metadata.getKey(), serializer );
		watcher.setObject( object, value );
		return watcher;
	}

	protected WrapperPlayServerEntityMetadata prepareUpdateMetadata( WrappedDataWatcher watcher ) {
		WrapperPlayServerEntityMetadata update = new WrapperPlayServerEntityMetadata();

		update.setEntityID( id );
		update.setMetadata( watcher.getWatchableObjects() );
		return update;
	}

	public int getID() {
		return id;
	}

	public static int getNextID() {
		return NEXT_ID;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void create() {
		WrapperPlayServerSpawnEntityLiving spawnMob = prepareCreate();

		for( Player player : players ) {
			sendPacket( player, spawnMob.getHandle() );
			this.createds.put( player.getName(), true );
		}
	}

	public void create( Player player ) {
		WrapperPlayServerSpawnEntityLiving spawnMob = prepareCreate();
		sendPacket( player, spawnMob.getHandle() );
		this.createds.put( player.getName(), true );
	}

	public WrapperPlayServerSpawnEntityLiving prepareCreate() {
		WrapperPlayServerSpawnEntityLiving spawnMob = new WrapperPlayServerSpawnEntityLiving();
		WrappedDataWatcher watcher = new WrappedDataWatcher();

		byte flags = (byte) ( ( visible ? (byte) 0 : METADATA_FLAGS_INVISIBLE ) | ( glowing ? METADATA_FLAGS_GLOWING : (byte) 0  ) );
		watcher = prepareMetadata( watcher, METADATA_FLAGS, flags );
		watcher = prepareMetadata( watcher, METADATA_SILENT, silent );
		//watcher = prepareMetadata( watcher, METADATA_NO_GRAVITY, !gravity );

		spawnMob.setEntityID( id );
		spawnMob.setType( type );
		spawnMob.setX( location.getX() );
		spawnMob.setY( location.getY() );
		spawnMob.setZ( location.getZ() );
		spawnMob.setMetadata( watcher );

		return spawnMob;
	}

	public WrapperPlayServerEntityDestroy prepareDestroy() {
		WrapperPlayServerEntityDestroy destroyMe = new WrapperPlayServerEntityDestroy();
		destroyMe.setEntityIds( new int[]{ id } );

		return destroyMe;
	}

	public void destroy() {
		WrapperPlayServerEntityDestroy destroyMe = prepareDestroy();

		for( Player player : players ) {
			sendPacket( player, destroyMe.getHandle() );
			this.createds.remove( player.getName() );
		}
	}

	public void destroy( Player player ) {
		WrapperPlayServerEntityDestroy destroyMe = prepareDestroy();
		sendPacket( player, destroyMe.getHandle() );
		this.createds.remove( player.getName() );
	}

	protected void sendPacket( Player player, PacketContainer packet ) {
		try {
			manager.sendServerPacket( player, packet );
		} catch( InvocationTargetException e ) {
			Bukkit.getLogger().log( Level.WARNING, "Cannot send " + packet + " to " + player, e );
		}
	}
}