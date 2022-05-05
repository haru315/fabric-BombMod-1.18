package net.fabricmc.bomb.entity;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.BigExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NovaEntity extends BombEntity {

    protected float size;

    public NovaEntity(EntityType<? extends NovaEntity> type, World world) {
        super(type, world);
        this.inanimate = true;
    }

    public NovaEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(BombMod.NOVA_BOMB_ENTITY, world);
        this.setPosition(x, y, z);
        this.setVelocity(0.0f, 0.2f, 0.0f);
        this.setFuse(300);
        this.size = 125.0f;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    @Override
    protected void explode() {
        BigExplosion explosion = new BigExplosion(this.world,this,this.getX(),this.getY(),this.getZ(),this.size);
        explosion.setCreateFire(true);
        explosion.ExplosionB();
    }
}