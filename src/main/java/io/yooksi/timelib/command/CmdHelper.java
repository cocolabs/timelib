/*
 *  Copyright (C) 2020 Matthew Cain
 *
 *  This file is part of TimeLib.
 *
 *  TimeLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TimeLib. If not, see <https://www.gnu.org/licenses/>.
 */
package io.yooksi.timelib.command;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class CmdHelper {

	/**
	 * Send chat message to player using given {@code CommandSource}.
	 */
	static void sendFeedback(CommandSource source, String message) {
		source.sendFeedback(new StringTextComponent(message), true);
	}
}
