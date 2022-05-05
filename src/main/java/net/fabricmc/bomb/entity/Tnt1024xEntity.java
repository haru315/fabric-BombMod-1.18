package net.fabricmc.bomb.entity;

import net.fabricmc.bomb.BombMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Tnt1024xEntity extends Tnt4xEntity {

    public Tnt1024xEntity(EntityType<? extends Tnt1024xEntity> type, World world) {
        super(type, world);
        this.inanimate = true;
    }

    public Tnt1024xEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(BombMod.TNT_1024x_ENTITY, world);
        this.setPosition(x, y, z);
        this.setVelocity(0.0f, 0.2f, 0.0f);
        this.setFuse(80);
        this.size = 128.0f;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }
}