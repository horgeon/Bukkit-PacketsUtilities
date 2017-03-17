/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface WrapperPlayServerCustomSoundEffect {

	/**
	 * Retrieve Sound Name.
	 *
	 * Notes: all known sound effect names can be seen here.
	 *
	 * @return The current Sound Name
	 */
	public String getSoundName();

	/**
	 * Set Sound Name.
	 *
	 * @param value - new value.
	 */
	public void setSoundName( String value );

	/**
	 * Retrieve Sound Category.
	 *
	 * Notes: the category that this sound will be played from (current
	 * categories)
	 *
	 * @return The current Sound Category
	 */
	public SoundCategory getSoundCategory();

	/**
	 * Set Sound Category.
	 *
	 * @param value - new value.
	 */
	public void setSoundCategory( SoundCategory value );

	/**
	 * Retrieve Effect Position X.
	 *
	 * Notes: effect X multiplied by 8 (fixed-point number with only 3 bits
	 * dedicated to the fractional part)
	 *
	 * @return The current Effect Position X
	 */
	public int getX();

	/**
	 * Set Effect Position X.
	 *
	 * Notes: effect X multiplied by 8 (fixed-point number with only 3 bits
	 * dedicated to the fractional part)
	 *
	 * @param value - new value.
	 */
	public void setX( int value );

	/**
	 * Retrieve Effect Position Y.
	 *
	 * Notes: effect Y multiplied by 8 (fixed-point number with only 3 bits
	 * dedicated to the fractional part)
	 *
	 * @return The current Effect Position Y
	 */
	public int getY();

	/**
	 * Set Effect Position Y.
	 *
	 * Notes: effect Y multiplied by 8 (fixed-point number with only 3 bits
	 * dedicated to the fractional part)
	 *
	 * @param value - new value.
	 */
	public void setY( int value );

	/**
	 * Retrieve Effect Position Z.
	 *
	 * Notes: effect Z multiplied by 8 (fixed-point number with only 3 bits
	 * dedicated to the fractional part)
	 *
	 * @return The current Effect Position Z
	 */
	public int getZ();

	/**
	 * Set Effect Position Z.
	 *
	 * Notes: effect Z multiplied by 8 (fixed-point number with only 3 bits
	 * dedicated to the fractional part)
	 *
	 * @param value - new value.
	 */
	public void setZ( int value );

	/**
	 * Retrieve Volume.
	 *
	 * Notes: 1 is 100%, can be more
	 *
	 * @return The current Volume
	 */
	public float getVolume();

	/**
	 * Set Volume.
	 *
	 * @param value - new value.
	 */
	public void setVolume( float value );

	/**
	 * Retrieve Pitch.
	 *
	 * Notes: 63 is 100%, can be more
	 *
	 * @return The current Pitch
	 */
	public float getPitch();

	/**
	 * Set Pitch.
	 *
	 * @param value - new value.
	 */
	public void setPitch( float value );

	/**
	 * Send the current packet to the given receiver.
	 *
	 * @param receiver - the receiver.
	 * @throws RuntimeException If the packet cannot be sent.
	 */
	public void sendPacket( Player receiver );
}