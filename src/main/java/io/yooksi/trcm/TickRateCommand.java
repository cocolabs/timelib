package io.yooksi.trcm;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class TickRateCommand {

	private static final String FEEDBACK_CHANGED = "Game tick rate changed from %.1f to %.1f";
	private static final String FEEDBACK_NOT_CHANGED = "Game tick rate is already %.1f";

	public static void register(CommandDispatcher<CommandSource> dispatcher) {

		dispatcher.register(Commands.literal("t").executes((c) -> setTickRate(c.getSource(), 20.0f))
				.then(Commands.argument("rate", FloatArgumentType.floatArg(0.1f))
						.executes((c) -> setTickRate(c.getSource(), c.getArgument("rate", Float.class)))));
	}

	private static int setTickRate(CommandSource source, float newRate) {

		final float currentRate = Tick.getRate();
		if (newRate != currentRate) {
			String message = String.format(FEEDBACK_CHANGED, currentRate, newRate);
			source.sendFeedback(new StringTextComponent(message), true);
			return (int) Tick.changeRate(newRate);
		}
		else {
			String message = String.format(FEEDBACK_NOT_CHANGED, currentRate);
			source.sendFeedback(new StringTextComponent(message), true);
			return (int) currentRate;
		}
	}
}
