package net.fabricmc.bomb.explosion;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class NuclearExplosion extends BombExplosion {

    public NuclearExplosion(World world,Entity entity, double x, double y, double z, float power) {
        super(world,entity, x, y, z, power);
    }

    public void Explosion(){
        int size = (int)this.power;
        for(int ix = size; ix >= -size; ix--){
            for(int iz = size; iz >= -size; iz--){
                for(int iy = size; iy >= -size; iy--){

                    float i = ix * ix + iz * iz + iy * iy;

                    if(i <= size*size){
                        //縁をランダム化
                        if(i >= Math.pow(size-1, 2) && random.nextFloat() > 0.5f){
                            continue;
                        }
                        // 対象外ブロック
                        BlockState getBlock = world.getBlockState(pos.add(ix,iy,iz));
                        if(getBlock == Blocks.AIR.getDefaultState() || getBlock == Blocks.BEDROCK.getDefaultState()){
                            continue;
                        }
                        world.setBlockState(pos.add(ix,iy,iz), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        float j = this.power;
        int k = MathHelper.floor(this.x - (double)j - 1.0);
        int l = MathHelper.floor(this.x + (double)j + 1.0);
        int d = MathHelper.floor(this.y - (double)j - 1.0);
        int q = MathHelper.floor(this.y + (double)j + 1.0);
        int e = MathHelper.floor(this.z - (double)j - 1.0);
        int r = MathHelper.floor(this.z + (double)j + 1.0);
        List<Entity> f = this.world.getOtherEntities(null, new Box(k, d, e, l, q, r));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
        for (int g = 0; g < f.size(); ++g) {
            double u;
            double t;
            double s;
            double blockPos;
            double h;
            Entity entity = f.get(g);
            if (entity.isImmuneToExplosion() || !((h = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double)j) <= 1.0) || (blockPos = Math.sqrt((s = entity.getX() - this.x) * s + (t = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y) * t + (u = entity.getZ() - this.z) * u)) == 0.0) continue;
            double v = this.power * 2.0F;

            entity.damage(this.damageSource,this.power * 2.0F);
        }
    }
}
