package fr.horgeon.bukkit.packetsutilities;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	private int versionMajorRelease = 1;
	private int versionMinorRelease = 0;

	private void buildPluginVersion() {
		PluginDescriptionFile pdf = getDescription();
		String version = pdf.getVersion();
		String[] versionNumbers = version.split( "\\." );
		if( versionNumbers.length > 0 )
			versionMajorRelease = Integer.valueOf( versionNumbers[ 0 ] );
		if( versionNumbers.length > 1 )
			versionMinorRelease = Integer.valueOf( versionNumbers[ 1 ] );
	}

	public int getVersionMajorRelease() {
		return versionMajorRelease;
	}

	public int getVersionMinorRelease() {
		return versionMinorRelease;
	}

	@Override
	public void onEnable() {
		buildPluginVersion();
	}

	@Override
	public void onDisable() {}
}
