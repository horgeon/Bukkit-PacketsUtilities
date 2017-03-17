# Bukkit Packets Utilities [![Build Status](https://travis-ci.org/horgeon/Bukkit-PacketsUtilities.svg?branch=master)](https://travis-ci.org/horgeon/Bukkit-PacketsUtilities)
Utility functions for Bukkit plugins using network packets.

- Fully-packets driven Scoreboard system
- Play and stop sounds commands with sound categories, compatible with Minecraft v1.9 and upward
- Easy way to access client's Protocol Version

## Usage
This library is available on my Maven repository.
```
<repositories>
	<repository>
		<id>horgeon-repo</id>
		<url>https://repo.horgeon.fr/repository/maven-releases/</url>
	</repository>
</repositories>
<dependencies>
	<dependency>
		<groupId>fr.horgeon.bukkit</groupId>
		<artifactId>packets-utilities</artifactId>
		<version>1.0</version>
	</dependency>
</dependencies>
```

## License

To comply with ProtocolLib and PacketWrapper's licenses, this project is distributed under the GPL version 3 license. A copy of the license is provided in LICENSE.md.

## Credits
- zyuiop for creating the original Scoreboard system
- aadnk, dmulloy2 and other contributors for ProtocolLib and PacketWrapper