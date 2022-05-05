package net.fabricmc.bomb.client.Renderer;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.entity.SuperNuclearBombEntity;
import net.fabricmc.bomb.entity.Tnt4xEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;


public class Tnt4xEntityRenderer extends EntityRenderer<Tnt4xEntity> {

    public Tnt4xEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.5f;
    }

    @Override
    public Identifier getTexture(Tnt4xEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    public void render(Tnt4xEntity Entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0, 0.5, 0.0);
        int j = Entity.getFuse();
        if ((float)j - g + 1.0f < 10.0f) {
            float h = 1.0f - ((float)j - g + 1.0f) / 10.0f;
            h = MathHelper.clamp(h, 0.0f, 1.0f);
            h *= h;
            h *= h;
            float k = 1.0f + h * 0.3f;
            matrixStack.scale(k, k, k);
        }
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0f));
        matrixStack.translate(-0.5, -0.5, 0.5);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
        TntMinecartEntityRenderer.renderFlashingBlock(BombMod.TNT4X_BLOCK.getDefaultState(), matrixStack, vertexConsumerProvider, i, j / 5 % 2 == 0);
        matrixStack.pop();
        super.render(Entity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
