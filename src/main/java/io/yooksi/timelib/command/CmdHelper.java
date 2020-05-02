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
