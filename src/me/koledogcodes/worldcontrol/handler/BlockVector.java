package me.koledogcodes.worldcontrol.handler;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BlockVector {

	public BlockVector(){
	}
	
	/**
	 * Sets random blocks between two points as a filled cube.
	 * @param world - World
	 * @param loc1 - First position 
	 * @param loc2 - Second position
	 * @param blocks - Blocks that should be randomly placed within the cube.
	 */
	
	@SuppressWarnings("unused")
	public void setRandomBlock(World world, Location loc1, Location loc2, Object[] blocks){
	double startX = loc1.getX();
	double startY = loc1.getY();
	double startZ = loc1.getZ();

    double maxX = Math.max(loc1.getX(), loc2.getX());
    double maxY = Math.max(loc1.getY(), loc2.getY());
    double maxZ = Math.max(loc1.getZ(), loc2.getZ());

    double minX = Math.min(loc1.getX(), loc2.getX());
    double minY = Math.min(loc1.getY(), loc2.getY());
    double minZ = Math.min(loc1.getZ(), loc2.getZ());

    for(int x = (int) minX; x <= maxX; x++){
    	for(int y = (int) minY; y <= maxY; y++){
    		for(int z = (int) minZ; z <= maxZ; z++){
    			Location loc = new Location(world, x, y, z);
    			loc.getBlock().setType((Material) blocks[new Random().nextInt(blocks.length)]);
      	   		}
        	}
		}
	}
}
