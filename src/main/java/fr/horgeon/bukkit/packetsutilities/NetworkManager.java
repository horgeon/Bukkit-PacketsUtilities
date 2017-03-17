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

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NetworkManager {
	private static Method getHandleMethod;
	private static Field playerConnectionField, networkManagerField;

	public static Object getHandle( Player player ) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		if( getHandleMethod == null ) {
			getHandleMethod = player.getClass().getDeclaredMethod( "getHandle" );
			getHandleMethod.setAccessible( true );
		}
		return getHandleMethod.invoke( player );
	}

	public static Object playerConnection( Object entityPlayer ) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		if( playerConnectionField == null ) {
			playerConnectionField = entityPlayer.getClass().getDeclaredField( "playerConnection" );
			playerConnectionField.setAccessible( true );
		}
		return playerConnectionField.get( entityPlayer );
	}

	public static Object networkManager( Object playerConnection ) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		if( networkManagerField == null ) {
			networkManagerField = playerConnection.getClass().getDeclaredField( "networkManager" );
			networkManagerField.setAccessible( true );
		}
		return networkManagerField.get( playerConnection );
	}

	public static int getVersion( Player player ) {
		return ProtocolLibrary.getProtocolManager().getProtocolVersion( player );
	}
}
