package fr.horgeon.bukkit.packetsutilities.entities;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FakeEntityEffect {
	public static final int MAX_TIME = 1000000;

	private WrapperPlayServerEntityEffect packet;

	public enum Type {
		ABSORPTION( (byte) 22, PotionEffectType.ABSORPTION ),
		UNLUCK( (byte) 27, PotionEffectType.UNLUCK ),
		BLINDNESS( (byte) 15, PotionEffectType.BLINDNESS ),
		FIRE_RESISTANCE( (byte) 12, PotionEffectType.FIRE_RESISTANCE ),
		GLOWING( (byte) 24, PotionEffectType.GLOWING ),
		FAST_DIGGING( (byte) 3, PotionEffectType.FAST_DIGGING ),
		HEALTH_BOOST( (byte) 21, PotionEffectType.HEALTH_BOOST ),
		HUNGER( (byte) 17, PotionEffectType.HUNGER ),
		HARM( (byte) 7, PotionEffectType.HARM ),
		HEAL( (byte) 6, PotionEffectType.HEAL ),
		INVISIBILITY( (byte) 14, PotionEffectType.INVISIBILITY ),
		JUMP( (byte) 8, PotionEffectType.JUMP ),
		LEVITATION( (byte) 25, PotionEffectType.LEVITATION ),
		LUCK( (byte) 26, PotionEffectType.LUCK ),
		SLOW_DIGGING( (byte) 4, PotionEffectType.SLOW_DIGGING ),
		CONFUSION( (byte) 9, PotionEffectType.CONFUSION ),
		NIGHT_VISION( (byte) 16, PotionEffectType.NIGHT_VISION ),
		POISON( (byte) 19, PotionEffectType.POISON ),
		REGENERATION( (byte) 10, PotionEffectType.REGENERATION ),
		DAMAGE_RESISTANCE( (byte) 11, PotionEffectType.DAMAGE_RESISTANCE ),
		SATURATION( (byte) 23, PotionEffectType.SATURATION ),
		SLOW( (byte) 2, PotionEffectType.SLOW ),
		SPEED( (byte) 1, PotionEffectType.SPEED ),
		INCREASE_DAMAGE( (byte) 5, PotionEffectType.INCREASE_DAMAGE ),
		WATER_BREATHING( (byte) 13, PotionEffectType.WATER_BREATHING ),
		WEAKNESS( (byte) 18, PotionEffectType.WEAKNESS ),
		WITHER( (byte) 20, PotionEffectType.WITHER );

		final byte id;
		final PotionEffectType equivalent;

		Type( byte id, PotionEffectType equivalent ) {
			this.id = id;
			this.equivalent = equivalent;
		}
	}

	public FakeEntityEffect( int entityId, Type effect, byte amplifier, int duration, boolean hideParticules ) {
		packet = new WrapperPlayServerEntityEffect();
		packet.setEntityID( entityId );
		packet.setEffectID( effect.id );
		packet.setAmplifier( amplifier );
		packet.setDuration( duration );
		packet.setHideParticles( hideParticules );
	}

	public FakeEntityEffect( int entityId, Type effect, byte amplifier, int duration ) {
		this( entityId, effect, amplifier, duration, true );
	}

	public FakeEntityEffect( int entityId, Type effect, int duration ) {
		this( entityId, effect, (byte) 0, duration, true );
	}

	public void send( Player player ) {
		packet.sendPacket( player );
	}
}