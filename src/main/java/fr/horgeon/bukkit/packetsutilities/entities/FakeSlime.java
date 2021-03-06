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

	public FakeSlime( Location location ) {
		super( EntityType.SLIME, location );
	}

	public FakeSlime( Location location, UUID uuid ) {
		super( EntityType.SLIME, location, uuid );
	}

	public FakeSlime( Location location, int id, UUID uuid ) {
		super( EntityType.SLIME, location, id, uuid );
	}

	public void setSize( int size ) {
		// Change size
		this.size = size;
		setMetadata( METADATA_SLIME_SIZE, size );
	}

	@Override
	protected WrappedDataWatcher prepareCreateMetadata( WrappedDataWatcher watcher ) {
		watcher = prepareMetadata( watcher, METADATA_SLIME_SIZE, size );
		return super.prepareCreateMetadata( watcher );
	}
}
