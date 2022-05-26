package net.fabricmc.bomb.client.Renderer.explosion;

import net.fabricmc.bomb.entity.BombEntity;
import net.fabricmc.bomb.entity.explosion.SphereExplosionEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SphereExplosionRender extends EntityRenderer<SphereExplosionEntity> {

    public SphereExplosionRender(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SphereExplosionEntity entity) {return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;}


    public void render(BombEntity Entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

    }
}
