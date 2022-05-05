package net.fabricmc.bomb.entity;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.NuclearExplosion;
import net.minecraft.entity.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SuperNuclearBombEntity extends BombEntity{

    public SuperNuclearBombEntity(EntityType<? extends SuperNuclearBombEntity> type, World world) {
        super(type, world);
        this.inanimate = true;
    }

    public SuperNuclearBombEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(BombMod.SUPER_NUCLEAR_BOMB_ENTITY, world);
        this.setPosition(x, y, z);
        this.setVelocity(0.0f, 0.2f, 0.0f);
        this.setFuse(300);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    @Override
    protected void explode() {
        int size = 150;
        NuclearExplosion explosion = new NuclearExplosion(this.world,this,this.getX(),this.getY(),this.getZ(),size);
        explosion.Explosion();
    }
}