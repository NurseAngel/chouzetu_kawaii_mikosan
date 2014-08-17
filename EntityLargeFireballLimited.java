package com.github.nurseangel.chouzetu_kawaii_mikosan;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.world.World;

/**
 * @author ふるりん
 * @version 1.0
 *          for Minecraft ver1.7.10
 *          エンダードラゴン用のLargeFireball
 *          このFireballは、10秒経過すると自動消滅します。
 */
public class EntityLargeFireballLimited extends EntityLargeFireball {

	private int ticksAlive;

	public EntityLargeFireballLimited(World par1World) {
		super(par1World);
		ticksAlive = 0;
	}

	public EntityLargeFireballLimited(World par1World, double par2,
			double par4, double par6, double par8, double par10, double par12) {
		super(par1World, par2, par4, par6, par8, par10, par12);
		ticksAlive = 0;
	}

	public EntityLargeFireballLimited(World par1World,
			EntityLiving par2EntityLiving, double par3, double par5, double par7) {
		super(par1World, par2EntityLiving, par3, par5, par7);
		ticksAlive = 0;
	}

	public void onUpdate()
	{
		super.onUpdate();

		++this.ticksAlive;

		// If it passes for 10 seconds(200 ticks), delete this fireball.
		if (this.ticksAlive >= 200)
		{
			this.setDead();
		}
	}
}
