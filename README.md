# Fresh Anvils

A separate Fabric mod for Minecraft Java Edition 26.3 that removes escalating anvil costs caused by prior work.

## Behavior

Every anvil operation treats both inputs as having a prior-work penalty of zero. The output also has a zero prior-work penalty. This applies to repairs, combining items, applying or combining enchanted books, and renaming. Items modified before installing the mod are supported automatically when used in an anvil.

Normal XP costs for the current operation, material consumption, enchantment compatibility, and anvil damage remain vanilla. The vanilla 39-level limit remains: an operation whose base cost is 40 or more may still show **Too Expensive** even with no prior-work penalty. Existing enchantments and durability are preserved normally; this mod only changes repair-history costs. Input items are not modified merely by previewing a recipe.

## Installation

Requires Minecraft **26.3**, Fabric Loader **0.19.5+**, and Java **25+**. Fabric API is not required.

Put `fresh-anvils-1.0.0.jar` in your instance's `mods` folder. It works alongside Nourishment. Single-player uses the built-in server. In multiplayer, install it on the server and clients so calculations and previews agree; client-only installation cannot change a vanilla server's costs.

## Build

Run `gradlew.bat build` on Windows or `sh gradlew build` on macOS/Linux with JDK 25. The mod jar is in `build/libs/`.

## Validation and playtest

Compiled against Minecraft 26.3. Inspected its anvil bytecode: the five repair-cost reads in `AnvilMenu.createResult` are intercepted, and increased repair cost returns zero. The mixin requires those targets to match. In-game validation remains to be performed.

Suggested checks in a disposable world with cheats:

1. Compare identical damaged items with different repair histories: repairing them using the same material quantity should cost the same XP.
2. Apply identical enchanted books to otherwise identical items with different histories: the costs should match.
3. Repeat repairs and renaming: no extra cost should accumulate from history.
4. Check both input slots using enchanted books with high existing repair costs.
5. Verify taking the output still consumes XP and materials, and canceling the preview does not alter inputs.
6. Confirm normal enchantment compatibility rules remain, and test alongside Nourishment.

For example, create an item with existing history:

```mcfunction
/give @s minecraft:diamond_sword[minecraft:damage=100,minecraft:repair_cost=127]
```

Compare with the same sword using `minecraft:repair_cost=0`. After an anvil operation, the output's repair cost should be zero.
