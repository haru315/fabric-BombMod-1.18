package net.fabricmc.bomb.explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;

public class ExplosionNukeRay {

	protected static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();

	List<FloatTriplet> affectedBlocks = new ArrayList<>();
	int posX;
	int posY;
	int posZ;
	World world;

	int strength;
	int length;
	int processed;

	private int gspNumMax;
	private int gspNum;
	double gspX;
	double gspY;

	public boolean isAusf3Complete = false;

	/*[[unused]]
	int count;
	int speed;
	int startY;
	int startCir;
	Random rand = new Random();
	private double overrideRange = 0;
	*/

	public ExplosionNukeRay(World world, int x, int y, int z, int strength, int count, int length) {
		this.world = world;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.strength = strength;
		this.length = length;
		//Ausf3, must be double
		//Mk 4.5, must be int32

		// Total number of points
//		this.gspNumMax = (int)(2.5 * Math.PI * Math.pow(this.length*2,2));
		this.gspNumMax = count;
		this.gspNum = 1;

		// The beginning of the generalized spiral points
		this.gspX = Math.PI;
		this.gspY = 0.0;


		/*[[unused]]
		// this.startY = strength;
		this.startY = 0;
		this.startCir = 0;

		this.count = count;
		this.speed = speed;
		
		//starts at around 80, becomes 8 at length 500
		this.overrideRange = Math.max((Math.log(length) * 4 - 2.5D) * 10, 0);
		*/
	}

	// Raise one generalized spiral points
	private void generateGspUp(){
		if (this.getGspNum() < this.getGspNumMax()) {
			int k = this.getGspNum() + 1;
			double hk = -1.0 + 2.0 * (k - 1.0) / (this.getGspNumMax() - 1.0);
			this.gspX = Math.acos(hk);

			double prev_lon = this.gspY;
			double lon = prev_lon + 3.6 / Math.sqrt(this.getGspNumMax()) / Math.sqrt(1.0 - hk * hk);
			this.gspY = lon % (Math.PI * 2);
		} else {
			this.gspX = 0.0;
			this.gspY = 0.0;
		}
		this.gspNum = this.getGspNum() + 1;
	}

	// Get Cartesian coordinates for spherical coordinates
	private Vec3d getSpherical2cartesian(){
		double dx = Math.sin(this.gspX) * Math.cos(this.gspY);
		double dz = Math.sin(this.gspX) * Math.sin(this.gspY);
		double dy = Math.cos(this.gspX);
		return new Vec3d(dx, dy, dz);
	}

	//currently used by mk4
	public void collectTipMk4_5(int count) {

		int amountProcessed = 0;

		while (this.getGspNumMax() >= this.getGspNum()){
			// Get Cartesian coordinates for spherical coordinates
			Vec3d vec = this.getSpherical2cartesian();

			int length = (int)Math.ceil(strength);
			float res = strength;
			FloatTriplet lastPos = null;

			for(int i = 0; i < length; i ++) {

				if(i > this.length)
					break;

				float x0 = (float) (posX + (vec.x * i));
				float y0 = (float) (posY + (vec.y * i));
				float z0 = (float) (posZ + (vec.z * i));

				double fac = 100 - ((double) i) / ((double) length) * 100;
				fac *= 0.07D;

				BlockPos blockPos = new BlockPos((int)x0, (int)y0, (int)z0);
				BlockState blockState = this.world.getBlockState(blockPos);
				FluidState fluidState = this.world.getFluidState(blockPos);

				if(!blockState.getMaterial().isLiquid()) {
					Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(null, this.world, blockPos, blockState, fluidState);
					if (optional.isPresent())
						res -= Math.pow(optional.get(), 7.5D - fac);
				}else {
					res -= Math.pow(Blocks.AIR.getBlastResistance(), 7.5D - fac);
				}

				if(res > 0 && !blockState.isAir()) {
					lastPos = new FloatTriplet(x0, y0, z0);
				}

				if(res <= 0 || i + 1 >= this.length) {
					if(affectedBlocks.size() < Integer.MAX_VALUE - 100 && lastPos != null) {
						affectedBlocks.add(lastPos);
					}
					break;
				}
			}
			// Raise one generalized spiral points
			this.generateGspUp();

			amountProcessed++;
			if(amountProcessed >= count) {
				return;
			}
		}
		isAusf3Complete = true;
	}

	public void processTip(int count) {

		int processedBlocks = 0;
		int braker = 0;

		for(int l = 0; l < Integer.MAX_VALUE; l++) {

			if(processedBlocks >= count)
				return;

			if(braker >= count * 50)
				return;

			if(l > affectedBlocks.size() - 1)
				break;

			if(affectedBlocks.isEmpty())
				return;

			int in = affectedBlocks.size() - 1;

			float x = affectedBlocks.get(in).xCoord;
			float y = affectedBlocks.get(in).yCoord;
			float z = affectedBlocks.get(in).zCoord;

			BlockPos blockPos3 = new BlockPos((int)x, (int)y, (int)z);

			world.setBlockState(blockPos3, Blocks.AIR.getDefaultState());

			Vec3d vec = new Vec3d(x - this.posX, y - this.posY, z - this.posZ);
			double pX = vec.x / vec.length();
			double pY = vec.y / vec.length();
			double pZ = vec.z/ vec.length();

			for(int i = 0; i < vec.length(); i ++) {
				int x0 = (int)(posX + pX * i);
				int y0 = (int)(posY + pY * i);
				int z0 = (int)(posZ + pZ * i);
				BlockPos blockP = new BlockPos(x0, y0, z0);

				if(!world.isAir(blockP)) {
					world.setBlockState(blockP, Blocks.AIR.getDefaultState());
					processedBlocks++;
				}

				braker++;
			}

			affectedBlocks.remove(in);
		}

		processed += count;
	}

	
	public void deleteStorage() {
		this.affectedBlocks.clear();
	}
	
	public int getStoredSize() {
		return this.affectedBlocks.size();
	}
	
	public int getProgress() {
		return this.processed;
	}

	public int getGspNumMax() {
		return gspNumMax;
	}

	public int getGspNum() {
		return gspNum;
	}

	public class FloatTriplet {
		public float xCoord;
		public float yCoord;
		public float zCoord;
		
		public FloatTriplet(float x, float y, float z) {
			xCoord = x;
			yCoord = y;
			zCoord = z;
		}
	}

}
