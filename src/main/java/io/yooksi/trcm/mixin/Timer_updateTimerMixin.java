package io.yooksi.trcm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.yooksi.trcm.Tick;
import net.minecraft.util.Timer;

@SuppressWarnings("unused")
@Mixin(Timer.class)
public class Timer_updateTimerMixin {

	@Redirect(method = "func_74275_a", at = @At(value = "FIELD",
			target = "Lnet/minecraft/util/Timer;field_194149_e:F")
	)
	private float updateTickSpeed(Timer timer) {
		return Tick.process_entities ? Math.max(50.0f, Tick.getMspt()) : 50f;
	}
}
