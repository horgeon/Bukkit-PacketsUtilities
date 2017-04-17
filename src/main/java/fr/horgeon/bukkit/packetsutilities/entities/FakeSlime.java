package fr.horgeon.bukkit.packetsutilities.entities;

import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

public class FakeSlime extends FakeEntity {
	protected static final Map.Entry<Integer, Class> METADATA_SLIME_SIZE = new AbstractMap.SimpleImmutableEntry( 11, Integer.class );

	protected int size = 1;

	public FakeSlime( Location location, ProtocolManager manager ) {
		super( EntityType.SLIME, location, manager );
	}

	public FakeSlime( Location location, ProtocolManager manager, UUID uuid ) {
		super( EntityType.SLIME, location, manager, uuid );
	}

	public FakeSlime( Location location, ProtocolManager manager, int id, UUID uuid ) {
		super( EntityType.SLIME, location, manager, id, uuid );
	}

	public void setSize( int size ) {
		// Change size
		this.size = size;
		setMetadata( METADATA_SLIME_SIZE, size );
	}

	@Override
	public WrapperPlayServerSpawnEntityLiving prepareCreate() {
		WrapperPlayServerSpawnEntityLiving spawnMob = new WrapperPlayServerSpawnEntityLiving();
		WrappedDataWatcher watcher = new WrappedDataWatcher();

		byte flags = (byte) ( ( visible ? (byte) 0 : METADATA_FLAGS_INVISIBLE ) | ( glowing ? METADATA_FLAGS_GLOWING : (byte) 0  ) );
		watcher = prepareMetadata( watcher, METADATA_FLAGS, flags );
		watcher = prepareMetadata( watcher, METADATA_SILENT, silent );
		watcher = prepareMetadata( watcher, METADATA_SLIME_SIZE, size );
		//watcher = prepareMetadata( watcher, METADATA_NO_GRAVITY, !gravity );

		spawnMob.setEntityID( id );
		spawnMob.setType( type );
		spawnMob.setX( location.getX() );
		spawnMob.setY( location.getY() );
		spawnMob.setZ( location.getZ() );
		spawnMob.setMetadata( watcher );

		return spawnMob;
	}
}
