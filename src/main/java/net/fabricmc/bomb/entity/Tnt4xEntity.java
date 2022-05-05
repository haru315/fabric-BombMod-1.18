package net.fabricmc.bomb.entity;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.BigExplosion;
import net.minecraft.entity.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Tnt4xEntity  extends BombEntity {

    protected float size;

    public Tnt4xEntity(EntityType<? extends Tnt4xEntity> type, World world) {
        super(type, world);
        this.inanimate = true;
    }

    public Tnt4xEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(BombMod.TNT_4x_ENTITY, world);
        this.setPosition(x, y, z);
        this.setVelocity(0.0f, 0.2f, 0.0f);
        this.setFuse(80);
        this.size = 8.0f;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    @Override
    protected void explode() {
        BigExplosion explosion = new BigExplosion(this.world,this,this.getX(),this.getY(),this.getZ(),this.size);
        explosion.ExplosionA();
    }
}