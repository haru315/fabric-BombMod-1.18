package net.fabricmc.bomb.explosion;

import com.google.common.collect.Sets;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

public class BigNukeExplosion extends BombExplosion {
    protected float size;
    protected boolean particles = true;

    protected float densitySphere = 1.0F;
    protected float densityRay = 0.5F;

    int gss_num_max;
    int gss_num;
    double gss_x;
    double gss_y;

    public BigNukeExplosion(World world, Entity entity, double x, double y, double z, float power, float size) {
        super(world,entity, x, y, z, power);
        this.size = size;
    }

    private void resetGss(){
        // 点の総数
        this.gss_num_max = (int)(4 * Math.PI * Math.pow(this.size,2) * this.densitySphere);
        this.gss_num = 1;

        // 一般化螺旋集合のはじまり
        this.gss_x = Math.PI;
        this.gss_y = 0.0;
    }

    // 一般化螺旋集合を一つ進める
    private void generateGssUp(){
        if (this.gss_num < this.gss_num_max) {
            int k = this.gss_num + 1;
            double hk = -1.0 + 2.0 * (k - 1.0) / (this.gss_num_max - 1.0);
            this.gss_x = Math.acos(hk);

            double prev_lon = this.gss_y;
            double lon = prev_lon + 3.6 / Math.sqrt(this.gss_num_max) / Math.sqrt(1.0 - hk * hk);
            this.gss_y = lon % (Math.PI * 2);
        } else {
            this.gss_x = 0.0;
            this.gss_y = 0.0;
        }
        this.gss_num++;
    }

    //　レイを飛ばす
    private Set makeRay(double dx,double dy,double dz,Set set)
    {
        int length = (int)Math.ceil(this.size);

        float res = this.power;

        BlockPos blockPos;

        for(float i = 0; i < length; i += this.densityRay) {
            float x0 = (float) (this.x + (dx * i));
            float y0 = (float) (this.y + (dy * i));
            float z0 = (float) (this.z + (dz * i));

            blockPos = new BlockPos(x0, y0, z0);
            BlockState blockState = this.world.getBlockState(blockPos);
            FluidState fluidState = this.world.getFluidState(blockPos);

            double fac = 100 - ((double) i) / ((double) length) * 100;
            fac *= 0.07D;

            if(blockState.getMaterial() != Material.AIR) {
                if (!blockState.getMaterial().isLiquid()) {
                    Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(null, this.world, blockPos, blockState, fluidState);
                    if (optional.isPresent()) {
                        res -= Math.pow(optional.get(), 7.5D - fac) * this.densityRay;
                    }
                }
                if(res > 0.0F){
                    set.add(blockPos);
                }
            }else if (this.createFire){
                if (this.world.getBlockState(blockPos.down()).isOpaqueFullCube(this.world, blockPos.down())) {
                    set.add(blockPos);
                }
            }
        }
        return set;
    }

    //
    public void process(){
        long start = System.currentTimeMillis();
        long lastUpdate = System.currentTimeMillis();
        Set<BlockPos> set = Sets.newHashSet();

        // 一般化螺旋集合をリセット
        this.resetGss();
        System.out.println("Gss start ray: [" + this.gss_num_max + "]");

        while (this.gss_num_max >= this.gss_num) {
            //球面座標を直交座標に変換する
            double dx = Math.sin(this.gss_x) * Math.cos(this.gss_y);
            double dz = Math.sin(this.gss_x) * Math.sin(this.gss_y);
            double dy = Math.cos(this.gss_x);

            set = this.makeRay(dx,dy,dz,set);

            // 経過表示
            if(System.currentTimeMillis() - lastUpdate > 10000L) {
                lastUpdate = System.currentTimeMillis();
                System.out.println("Gss directed ray: [" + this.gss_num + "/" + this.gss_num_max + "]");
            }
            // 一般化螺旋集合を一個進める
            this.generateGssUp();
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


    //BigNukeExplosion
    public void Explosion(){
        this.effect();
        if (!this.world.isClient) {
            this.process();
        }
    }

    // エフェクト・パーティクル
    public void effect(){
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
        }
        if (particles) {
            if (this.power > 2.0f) {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
    }
}
