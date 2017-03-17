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

public class WrapperPlayServerCustomSoundEffect_v1_9 extends WrapperPlayServerCustomSoundEffect_v1_10 {

	public WrapperPlayServerCustomSoundEffect_v1_9() {
		super();
	}

	/**
	 * Retrieve Pitch.
	 *
	 * Notes: 63 is 100%, can be more
	 *
	 * @return The current Pitch
	 */
	@Override
	public float getPitch() {
		return handle.getFloat().read( 3 );
	}

	/**
	 * Set Pitch.
	 *
	 * @param value - new value.
	 */
	@Override
	public void setPitch( float value ) {
		handle.getIntegers().write( 3, Math.round( value * 63 ) ); // 63 = 100%
	}
}