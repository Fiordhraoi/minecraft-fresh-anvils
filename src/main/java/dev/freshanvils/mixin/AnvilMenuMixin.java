package dev.freshanvils.mixin;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
abstract class AnvilMenuMixin {
    // Ignore existing penalties, including those on items obtained before installing the mod.
    // Scope this redirect to the anvil calculation; inventory items are not mutated in preview.
    @Redirect(method = "createResult", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"),
            require = 5, allow = 5)
    private <T> T freshAnvils$ignorePriorWork(ItemStack stack, DataComponentType<T> type, T fallback) {
        if (type == DataComponents.REPAIR_COST) {
            @SuppressWarnings("unchecked") T zero = (T) Integer.valueOf(0);
            return zero;
        }
        return stack.getOrDefault(type, fallback);
    }

    // Repairs and enchantment merges must not introduce a new penalty on the output.
    @Inject(method = "calculateIncreasedRepairCost", at = @At("HEAD"), cancellable = true)
    private static void freshAnvils$noNewPenalty(int previousCost, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }
}
