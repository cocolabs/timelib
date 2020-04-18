package io.yooksi.trcm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import io.yooksi.trcm.Tick;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class TickRateCommand {

	private static final String FEEDBACK_CHANGED = "Game tick rate changed from %.1f to %.1f";
	private static final String FEEDBACK_NOT_CHANGED = "Game tick rate is already %.1f";
	private static final String FEEDBACK_RESET = "Game tick rate has been reset to %.1f";

	public static void register(CommandDispatcher<CommandSource> dispatcher) {

		dispatcher.register(Commands.literal("t").executes((c) -> setTickRate(c.getSource()))
				.then(Commands.argument("rate", FloatArgumentType.floatArg(0.1f, 20.0f))
						.executes((c) -> setTickRate(c.getSource(), c.getArgument("rate", Float.class)))));
	}
	/** Change tick rate to new value */
	private static int setTickRate(CommandSource source, float newRate) {

		final float currentRate = Tick.getRate();
		if (newRate != currentRate) {
			sendFeedback(source, String.format(FEEDBACK_CHANGED, currentRate, newRate));
			return (int) Tick.changeRate(newRate);
		}
		else {
			sendFeedback(source, String.format(FEEDBACK_NOT_CHANGED, currentRate));
			return (int) currentRate;
		}
	}
	/** Reset tick rate to a default value */
	private static int setTickRate(CommandSource source) {

		final float defaultRate = Tick.resetRate();
		sendFeedback(source, String.format(FEEDBACK_RESET, defaultRate));
		return (int) defaultRate;

	}
	/** Send chat message to player */
	private static void sendFeedback(CommandSource source, String message) {
		source.sendFeedback(new StringTextComponent(message), true);
	}
}
