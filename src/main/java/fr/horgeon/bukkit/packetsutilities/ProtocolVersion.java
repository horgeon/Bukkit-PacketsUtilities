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

import com.comphenix.protocol.reflect.IntEnum;

public class ProtocolVersion extends IntEnum {
	public static final int v17w06a = 317;
	public static final int v1_11_2 = 316;
	public static final int v1_11_1 = 316;
	public static final int v16w50a = 316;
	public static final int v1_11 = 315;
	public static final int v1_11_pre1 = 314;
	public static final int v16w44a = 313;
	public static final int v16w43a = 313;
	public static final int v16w42a = 312;
	public static final int v16w41a = 311;
	public static final int v16w40a = 310;
	public static final int v16w39c = 309;
	public static final int v16w39b = 308;
	public static final int v16w39a = 307;
	public static final int v16w38a = 306;
	public static final int v16w36a = 305;
	public static final int v16w35a = 304;
	public static final int v16w33a = 303;
	public static final int v16w32b = 302;
	public static final int v16w32a = 301;
	public static final int v1_10_2 = 210;
	public static final int v1_10_1 = 210;
	public static final int v1_10 = 210;
	public static final int v1_10_pre2 = 205;
	public static final int v1_10_pre1 = 204;
	public static final int v16w21b = 203;
	public static final int v16w21a = 202;
	public static final int v16w20a = 201;
	public static final int v1_9_4 = 110;
	public static final int v1_9_3 = 110;
	public static final int v1_9_3_pre3 = 110;
	public static final int v1_9_3_pre2 = 110;
	public static final int v1_9_3_pre1 = 109;
	public static final int v16w15b = 109;
	public static final int v16w15a = 109;
	public static final int v16w14a = 109;
	public static final int v1_9_2 = 109;
	public static final int v1_RV_Pre1 = 108;
	public static final int v1_9_1 = 108;
	public static final int v1_9_1_pre3 = 108;
	public static final int v1_9_1_pre2 = 108;
	public static final int v1_9_1_pre1 = 107;
	public static final int v1_9 = 107;
	public static final int v1_9_pre4 = 106;
	public static final int v1_9_pre3 = 105;
	public static final int v1_9_pre2 = 104;
	public static final int v1_9_pre1 = 103;
	public static final int v16w07b = 102;
	public static final int v16w07a = 101;
	public static final int v16w06a = 100;
	public static final int v16w05b = 99;
	public static final int v16w05a = 98;
	public static final int v16w04a = 97;
	public static final int v16w03a = 96;
	public static final int v16w02a = 95;
	public static final int v15w51b = 94;
	public static final int v15w51a = 93;
	public static final int v15w50a = 92;
	public static final int v15w49b = 91;
	public static final int v15w49a = 90;
	public static final int v15w47c = 89;
	public static final int v15w47b = 88;
	public static final int v15w47a = 87;
	public static final int v15w46a = 86;
	public static final int v15w45a = 85;
	public static final int v15w44b = 84;
	public static final int v15w44a = 83;
	public static final int v15w43c = 82;
	public static final int v15w43b = 81;
	public static final int v15w43a = 80;
	public static final int v15w42a = 79;
	public static final int v15w41b = 78;
	public static final int v15w41a = 77;
	public static final int v15w40b = 76;
	public static final int v15w40a = 75;
	public static final int v15w39c = 74;
	public static final int v15w38b = 73;
	public static final int v15w38a = 72;
	public static final int v15w37a = 71;
	public static final int v15w36d = 70;
	public static final int v15w36c = 69;
	public static final int v15w36b = 68;
	public static final int v15w36a = 67;
	public static final int v15w35e = 66;
	public static final int v15w35d = 65;
	public static final int v15w35c = 64;
	public static final int v15w35b = 63;
	public static final int v15w35a = 62;
	public static final int v15w34d = 61;
	public static final int v15w34c = 60;
	public static final int v15w34b = 59;
	public static final int v15w34a = 58;
	public static final int v15w33c = 57;
	public static final int v15w33b = 56;
	public static final int v15w33a = 55;
	public static final int v15w32c = 54;
	public static final int v15w32b = 53;
	public static final int v15w32a = 52;
	public static final int v15w31c = 51;
	public static final int v15w31b = 50;
	public static final int v15w31a = 49;
	public static final int v15w14a = 48;
	public static final int v1_8_9 = 47;
	public static final int v1_8_8 = 47;
	public static final int v1_8_7 = 47;
	public static final int v1_8_6 = 47;
	public static final int v1_8_5 = 47;
	public static final int v1_8_4 = 47;
	public static final int v1_8_3 = 47;
	public static final int v1_8_2 = 47;
	public static final int v1_8_1 = 47;
	public static final int v1_8 = 47;
	public static final int v1_8_pre3 = 46;
	public static final int v1_8_pre2 = 45;
	public static final int v1_8_pre1 = 44;
	public static final int v14w34d = 43;
	public static final int v14w34c = 42;
	public static final int v14w34b = 41;
	public static final int v14w34a = 40;
	public static final int v14w33c = 39;
	public static final int v14w33b = 38;
	public static final int v14w33a = 37;
	public static final int v14w32d = 36;
	public static final int v14w32c = 35;
	public static final int v14w32b = 34;
	public static final int v14w32a = 33;
	public static final int v14w31a = 32;
	public static final int v14w30c = 31;
	public static final int v14w30a = 30;
	public static final int v14w29a = 29;
	public static final int v14w28b = 28;
	public static final int v14w28a = 27;
	public static final int v14w27b = 26;
	public static final int v14w27a = 26;
	public static final int v14w26c = 25;
	public static final int v14w26b = 24;
	public static final int v14w26a = 23;
	public static final int v14w25b = 22;
	public static final int v14w25a = 21;
	public static final int v14w21b = 20;
	public static final int v14w21a = 19;
	public static final int v14w20a = 18;
	public static final int v14w19a = 17;
	public static final int v14w18b = 16;
	public static final int v14w17a = 15;
	public static final int v14w11a = 14;
	public static final int v14w10c = 13;
	public static final int v14w10b = 13;
	public static final int v14w10a = 13;
	public static final int v14w08a = 12;
	public static final int v14w07a = 11;
	public static final int v14w06a = 10;
	public static final int v14w05a = 9;
	public static final int v14w04b = 8;
	public static final int v14w04a = 7;
	public static final int v14w03a = 6;
	public static final int v14w02a = 5;
	public static final int v1_7_10 = 5;
	public static final int v1_7_9 = 5;
	public static final int v1_7_8 = 5;
	public static final int v1_7_7 = 5;
	public static final int v1_7_6 = 5;
	public static final int v1_7_5 = 4;
	public static final int v1_7_4 = 4;
	public static final int v1_7_3_pre = 4;
	public static final int v1_7_2 = 4;
	public static final int v1_7_1_pre = 4;
	public static final int v13w43a = 2;
	public static final int v13w42b = 1;
	public static final int v13w42a = 1;
	public static final int v13w41b = 0;
	public static final int v13w41a = 0;

	private static final ProtocolVersion INSTANCE = new ProtocolVersion();

	public static ProtocolVersion getInstance() {
		return INSTANCE;
	}
}
