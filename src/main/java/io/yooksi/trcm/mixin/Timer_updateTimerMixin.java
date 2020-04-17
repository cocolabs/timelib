package io.yooksi.trcm.mixin;

import io.yooksi.trcm.Tick;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(Timer.class)
public class Timer_updateTimerMixin {

	@Redirect(method = "updateTimer", at = @At(value = "FIELD",
			target = "Lnet/minecraft/util/Timer;tickLength:F")
	)
	private float updateTickSpeed(Timer timer) {
		return Tick.process_entities ? Math.max(50.0f, Tick.getMspt()) : 50f;
	}
}
