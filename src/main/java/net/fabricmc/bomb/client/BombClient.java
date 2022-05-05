package net.fabricmc.bomb.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.client.Renderer.*;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class BombClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // エンティティレンダー登録
        // Tnt_x
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_4x_ENTITY, (context) -> {
            return new Tnt4xEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_16x_ENTITY, (context) -> {
            return new Tnt16xEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_64x_ENTITY, (context) -> {
            return new Tnt64xEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_256x_ENTITY, (context) -> {
            return new Tnt256xEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_1024x_ENTITY, (context) -> {
            return new Tnt1024xEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_4096x_ENTITY, (context) -> {
            return new Tnt4096xEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.TNT_16384x_ENTITY, (context) -> {
            return new Tnt16384xEntityRenderer(context);
        });

        //NuclearBombs
        EntityRendererRegistry.INSTANCE.register(BombMod.NUCLEAR_BOMB_ENTITY, (context) -> {
            return new NuclearBombEntityRenderer(context);
        });
        EntityRendererRegistry.INSTANCE.register(BombMod.SUPER_NUCLEAR_BOMB_ENTITY, (context) -> {
            return new SuperNuclearBombEntityRenderer(context);
        });

        //NovaBombs
        EntityRendererRegistry.INSTANCE.register(BombMod.NOVA_BOMB_ENTITY, (context) -> {
            return new NovaEntityRenderer(context);
        });
    }
}
