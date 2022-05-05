package net.fabricmc.bomb;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup BOMB_GROUP = FabricItemGroupBuilder.build(new Identifier(BombMod.MOD_ID, "bomb_mod"),
            () -> new ItemStack(BombMod.NUCLEAR_BOMB));
}
