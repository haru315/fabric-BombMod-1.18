package net.fabricmc.bomb.explosion;

import com.google.common.collect.Sets;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BigExplosion extends BombExplosion {
    protected int mode = 0;
    protected float attenuationVar1 = 0.22500001f;
    protected float attenuationVar2 = 0.3F;

    public BigExplosion(World world, Entity entity, double x, double y, double z, float power) {
        super(world,entity, x, y, z, power);
    }

    private Set makeRay(int j, int k, int l, int smoothness, Set set)
    {
        double d0 = ((float)j / ((float)smoothness - 1.0F)) * 2.0F - 1.0F;
        double d1 = ((float)k / ((float)smoothness - 1.0F)) * 2.0F - 1.0F;
        double d2 = ((float)l / ((float)smoothness - 1.0F)) * 2.0F - 1.0F;
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d0 /= d3;
        d1 /= d3;
        d2 /= d3;
        float f = randomRay(this.power,this.mode);
        double d4 = this.x;
        double d6 = this.y;
        double d8 = this.z;
        BlockPos blockPos = new BlockPos(d4, d6, d8);
        for(; f > 0.0F; f -= this.attenuationVar1)
        {
            blockPos = new BlockPos(d4, d6, d8);
            BlockState blockState = world.getBlockState(blockPos);
            FluidState fluidState = this.world.getFluidState(blockPos);
            float f3 = 0.0F;
            if(blockState.getMaterial() != Material.AIR)
            {
                if(!blockState.getMaterial().isLiquid()) {
                    Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(null, this.world, blockPos, blockState, fluidState);
                    if (optional.isPresent()) {
                        f3 = optional.get().floatValue();
                    }
                    f -= attenuation(f3,this.mode);
                }
                if(f > 0.0F){
                    set.add(blockPos);
                }
            }else if (this.createFire){
                if (this.world.getBlockState(blockPos.down()).isOpaqueFullCube(this.world, blockPos.down())) {
                    set.add(blockPos);
                }
            }

            d4 += d0 * (double)this.attenuationVar2;
            d6 += d1 * (double)this.attenuationVar2;
            d8 += d2 * (double)this.attenuationVar2;
        }
        return set;
    }

    //
    public void ExplosionA(){
        int i = (int)(this.power * 2.0F);
        this.mode = 0;
        this.attenuationVar1 = 0.22500001F;
        this.attenuationVar2 = 0.3F;
        this.Explosion(i);
    }

    //
    public void ExplosionB(){
        int i = (int)(this.power * 2.048F);
        this.mode = 1;
        this.attenuationVar1 = 0.275F;
        this.attenuationVar2 = 0.45F;
        this.Explosion(i);
    }

    //
    public void Explosion(int smoothness){
        long start = System.currentTimeMillis();
        long lastUpdate = System.currentTimeMillis();
        Set<BlockPos> set = Sets.newHashSet();
        for(int l = 0; l < smoothness; l++)
        {
            if(System.currentTimeMillis() - lastUpdate > 10000L)
            {
                lastUpdate = System.currentTimeMillis();
                System.out.println("+-X directed ray: ["+(l + 1)+"/"+smoothness+"]");
            }
            for(int k = 0; k < smoothness; k++)
            {
                set = makeRay(0, k, l, smoothness, set);
                set = makeRay(smoothness - 1, k, l, smoothness, set);
            }
        }

        for(int j = 0; j < smoothness; j++)
        {
            if(System.currentTimeMillis() - lastUpdate > 10000L)
            {
                lastUpdate = System.currentTimeMillis();
                System.out.println("+-Y directed ray: ["+(j + 1)+"/"+smoothness+"]");
            }
            for(int l = 0; l < smoothness; l++)
            {
                set = makeRay(j, 0, l, smoothness, set);
                set = makeRay(j, smoothness - 1, l, smoothness, set);
            }
        }

        for(int j = 0; j < smoothness; j++)
        {
            if(System.currentTimeMillis() - lastUpdate > 10000L)
            {
                lastUpdate = System.currentTimeMillis();
                System.out.println("+-Z directed ray: ["+(j + 1)+"/"+smoothness+"]");
            }
            for(int k = 0; k < smoothness; k++)
            {
                set = makeRay(j, k, 0, smoothness, set);
                set = makeRay(j, k, smoothness - 1, smoothness, set);
            }
        }
        int size = set.size();
        int e = 0;
        for (BlockPos blockPos3 : set) {
            if(System.currentTimeMillis() - lastUpdate > 10000L) {
                lastUpdate = System.currentTimeMillis();
                System.out.println("Block:["+e+"/"+size+"]");
            }
            e++;
            world.setBlockState(blockPos3, Blocks.AIR.getDefaultState());
            if (this.createFire) {
                if (this.world.getBlockState(blockPos3.down()).isOpaqueFullCube(this.world, blockPos3.down())) {
                    this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
                }
            }
        }
    }

    // レイ、ランダム関数
    public float randomRay(float f0, int i){
        float f1 = switch (i) {
            case 0 -> f0 * (0.7f + this.world.random.nextFloat() * 0.6f);
            case 1 -> f0 * (1.0F + this.world.random.nextFloat() * 0.02F);
            default -> f0;
        };
        return f1;
    }

    // ブロック減衰関数
    public float attenuation(float f0, int i){
        float f1 = 0;
        switch (i) {
            case 0 :
                f1 = (f0 + 0.3f) * 0.3f;
                break;
            case 1 :
                if(f0 > 64F) f0 /= 16F;
                f1 = ((f0 / 3.0F) + 0.3F) * 0.3F;
                break;
            default :
                f1 = f0;
        }
        return f1;
    }
}
