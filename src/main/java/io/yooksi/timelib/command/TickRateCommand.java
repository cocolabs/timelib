package io.yooksi.timelib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import com.mojang.brigadier.context.CommandContext;
import io.yooksi.timelib.TickRate;
import io.yooksi.timelib.define.TickProfile;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class TickRateCommand {

	private static final String FEEDBACK_CHANGED = "Game tick rate changed from %.1f to %.1f";
	private static final String FEEDBACK_NOT_CHANGED = "Game tick rate is already %.1f";
	private static final String FEEDBACK_RESET = "Game tick rate has been reset to %.1f";

	public static void register(CommandDispatcher<CommandSource> dispatcher) {

		dispatcher.register(Commands.literal("tickrate").then(Commands.literal("set")
				.then(Commands.argument("value",
						FloatArgumentType.floatArg(0.1f, TickRate.MAXIMUM))
						.executes((c) -> setTickRate(c, c.getArgument("value", Float.class))))
				.then(Commands.literal("slow").executes((c) -> setTickRate(c, TickProfile.SLOW.rate)))
				.then(Commands.literal("normal").executes((c) -> setTickRate(c, TickProfile.DEFAULT.rate)))
				.then(Commands.literal("fast").executes((c) -> setTickRate(c, TickProfile.FAST.rate))))
				.then(Commands.literal("reset").executes(TickRateCommand::setTickRate)));
	}

	/**
	 * Change tick rate to new value
	 */
	private static int setTickRate(CommandContext<CommandSource> context, float newRate) {

		CommandSource source = context.getSource();
		final float currentRate = TickRate.get();

		if (newRate != currentRate) {
			CmdHelper.sendFeedback(source, String.format(FEEDBACK_CHANGED, currentRate, newRate));
			return (int) TickRate.set(newRate);
		}
		else {
			CmdHelper.sendFeedback(source, String.format(FEEDBACK_NOT_CHANGED, currentRate));
			return (int) currentRate;
		}
	}

	/**
	 * Reset tick rate to default value
	 */
	private static int setTickRate(CommandContext<CommandSource> context) {

		final float defaultRate = TickRate.reset();
		CmdHelper.sendFeedback(context.getSource(), String.format(FEEDBACK_RESET, defaultRate));
		return (int) defaultRate;
	}
}
