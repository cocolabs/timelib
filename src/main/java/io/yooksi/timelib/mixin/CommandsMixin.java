package io.yooksi.timelib.mixin;

import io.yooksi.timelib.command.TimeCycleCommand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import io.yooksi.timelib.command.TickRateCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

@Mixin(Commands.class)
public class CommandsMixin {

	@Shadow @Final
	private CommandDispatcher<CommandSource> dispatcher;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onRegister(boolean boolean_1, CallbackInfo ci) {

		TickRateCommand.register(dispatcher);
		TimeCycleCommand.register(dispatcher);
	}
}
