package io.yooksi.trcm;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class TickRateCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {

		dispatcher.register(Commands.literal("t").executes((c) -> setTickRate(c.getSource(), 20.0f))
				.then(Commands.argument("rate", FloatArgumentType.floatArg(0.1f))
				.executes((c) -> setTickRate(c.getSource(), c.getArgument("rate", Float.class)))));
	}

	private static int setTickRate(CommandSource source, float rate) {


		return (int) Tick.changeRate(rate);
	}
}
