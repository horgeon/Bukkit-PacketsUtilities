/**
 * Bukkit Packets Utilities - Utility functions for Bukkit plugins using network packets
 * Copyright (C) Horgeon <http://horgeon.fr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.horgeon.bukkit.packetsutilities;

import com.comphenix.packetwrapper.WrapperPlayServerCustomSoundEffect;
import com.comphenix.packetwrapper.WrapperPlayServerCustomSoundEffect_v1_10;
import com.comphenix.packetwrapper.WrapperPlayServerCustomSoundEffect_v1_9;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.netty.buffer.ByteBuf;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sounds {
	public static void playSound( Player p, Location loc, String sound, float volume, float pitch ) {
		playSound( p, loc, sound, SoundCategory.MASTER, volume, pitch );
	}

	public static void playSound( Player p, Location loc, String sound, SoundCategory category, float volume, float pitch ) {
		if( p == null || loc == null || sound == null || category == null ) return;

		WrapperPlayServerCustomSoundEffect soundPacket;
		if( NetworkManager.getVersion( p ) > ProtocolVersion.v1_9_4 ) {
			soundPacket = new WrapperPlayServerCustomSoundEffect_v1_10();
		} else {
			soundPacket = new WrapperPlayServerCustomSoundEffect_v1_9();
		}

		soundPacket.setSoundName( sound );
		soundPacket.setSoundCategory( EnumWrappers.SoundCategory.valueOf( category.name() ) );
		soundPacket.setVolume( volume );
		soundPacket.setPitch( pitch );
		soundPacket.setX( (int) loc.getX() << 8 );
		soundPacket.setY( (int) loc.getY() << 8 );
		soundPacket.setZ( (int) loc.getZ() << 8 );
		soundPacket.sendPacket( p );
	}

	public static void stopSound( Player p, String sound ) {
		stopSound( p, sound, null );
	}

	public static void stopSound( Player p, String sound, SoundCategory category ) {
		if( p == null || sound == null ) return;

		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		PacketContainer stopPacket = protocolManager.createPacket( PacketType.Play.Server.CUSTOM_PAYLOAD );
		stopPacket.getStrings().write( 0, "MC|StopSound" );

		ByteBuf packetDataSerializer = PacketContainer.createPacketBuffer();

		try {
			java.lang.reflect.Method a = packetDataSerializer.getClass().getMethod( "a", String.class );

			a.invoke( packetDataSerializer, category == null ? "" : EnumWrappers.SoundCategory.valueOf( category.name() ).getKey() );
			a.invoke( packetDataSerializer, sound );

			stopPacket.getModifier().write(1, packetDataSerializer );
			protocolManager.sendServerPacket( p, stopPacket );
		} catch( Exception e ) {
			System.err.println( "Cannot send stopAll sound packet." );
			System.err.println( e.getLocalizedMessage() );
		}
	}
}