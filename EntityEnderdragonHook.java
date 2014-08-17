package com.github.nurseangel.chouzetu_kawaii_mikosan;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityEnderdragonHook {

	/**
	 * エンダードラゴンのonLivingUpdateをフック
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if (!(event.entity instanceof EntityDragon)) {
			return;
		}
		EntityDragon dragon = (EntityDragon) event.entity;

		// 炎
		addFireCharge(dragon);

		// 落雷
		if (getCountEnderCrystal(dragon) < 1) {
			addDawnHammer(dragon);
		}

	}

	// ファイアーチャージの発射間隔の制御用
	public static int attackCounterFireball = 0;
	// 落雷の発射間隔の制御用（0で発射）※上記ファイアーチャージとカウント方法が異なるので注意
	public static int attackCounterLightningbolt = 100;
	// EntityDragon.randがprotected
	public Random rand = new Random();

	/**
	 * 炎
	 *
	 * @param dragon
	 */
	private void addFireCharge(EntityDragon dragon) {
		/**
		 * 元ソースではEntityDragon.targetだが、privateで取れないためplayerEntitiesで代用。
		 * 何故かリフレクションでも取れない(開発ではOKなのに本番でjava.lang.NoSuchFieldException: target)
		 * 結果えらい勢いで炎が飛んでくるようになったけどこれはこれでまあいいか。
		 *
		 */
		Entity target = (Entity) dragon.worldObj.playerEntities.get(
				rand.nextInt(dragon.worldObj.playerEntities.size()));

		// ファイアーチャージ攻撃コード（EntityGhast.javaから引用）
		// ターゲット（プレイヤー）からの距離が64m～32mの時、ファイアーチャージを射出します。
		double var10 = 64.0D;
		double var12 = 32.0D;

		if (target != null && target.getDistanceSqToEntity(dragon) <= var10 * var10
				&& target.getDistanceSqToEntity(dragon) > var12 * var12) // 32mより近い場合は射出しないようにします。
		{
			double var11 = target.posX - dragon.posX;
			double var13 = target.boundingBox.minY + (double) (target.height / 2.0F)
					- (dragon.posY + (double) (dragon.height / 2.0F));
			double var15 = target.posZ - dragon.posZ;

			if (dragon.canEntityBeSeen(target))
			{
				if (attackCounterFireball == 30)
				{
					dragon.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1007, (int) dragon.posX, (int) dragon.posY,
							(int) dragon.posZ, 0);
				}

				++attackCounterFireball;
				// FMLLog.info("attackCounterFireball: " +
				// attackCounterFireball);

				if (attackCounterFireball >= 60)
				{
					dragon.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, (int) dragon.posX, (int) dragon.posY,
							(int) dragon.posZ, 0);

					// ネザーは周囲が壁に囲まれていますが、ジエンドは開けた空間です。
					// EntityLargeFireballは何かに衝突しないと消滅しないため、ジエンドだとエンティティが残ってしまいます。
					// そのため、10秒経過すると自動消滅する、EntityLargeFireballLimitedクラスを新たに追加して使用します。
					EntityLargeFireballLimited var17 =
							new EntityLargeFireballLimited(dragon.worldObj, dragon, var11, var13, var15);
					// field_92057_eが爆発力。今回はガストと同じに設定しています。
					var17.field_92057_e = 1;

					double var18 = 4.0D;
					Vec3 var20 = dragon.getLook(1.0F);
					var17.posX = dragon.posX + var20.xCoord * var18;
					var17.posY = dragon.posY + (double) (dragon.height / 2.0F) + 0.5D;
					var17.posZ = dragon.posZ + var20.zCoord * var18;

					// ガストのファイアーチャージの３倍の速度にします。
					double var14 = 3;
					var17.accelerationX *= var14;
					var17.accelerationY *= var14;
					var17.accelerationZ *= var14;

					dragon.worldObj.spawnEntityInWorld(var17);

					attackCounterFireball = -40;
				}
			}
			else if (attackCounterFireball > 0)
			{
				--attackCounterFireball;
			}
		}
		else
		{
			if (attackCounterFireball > 0)
			{
				--attackCounterFireball;
			}
		}
	}

	/**
	 * ドーンハンマー
	 *
	 * @param dragon
	 */
	private void addDawnHammer(EntityDragon dragon) {

		// 落雷攻撃コード
		// エンダードラゴンの周囲にエンダークリスタルがない場合、落雷攻撃を行います。
		// プレイヤーの位置を取得
		Entity player2 = (Entity) dragon.worldObj.playerEntities.get(
				rand.nextInt(dragon.worldObj.playerEntities.size()));
		double targetX = player2.posX;
		double targetY = player2.posY;
		double targetZ = player2.posZ;

		// カウンターがゼロになったら攻撃
		--attackCounterLightningbolt;
		// FMLLog.info("attackCounterLightningbolt: "+attackCounterLightningbolt);

		if (attackCounterLightningbolt < 0) {

			int x = MathHelper.floor_double(targetX);
			int y = MathHelper.floor_double(targetY);
			int z = MathHelper.floor_double(targetZ);
			if (dragon.worldObj.canBlockSeeTheSky(x, y, z) == true) {
				EntityLightningBolt entitybolt = new EntityLightningBolt(dragon.worldObj, 0D, 0D, 0D);

				// 着弾範囲はx,zともに-8～+8。
				x = x - 8 + rand.nextInt(17);
				z = z - 8 + rand.nextInt(17);

				entitybolt.setLocationAndAngles(x, y - 1, z, 0, 0.0F);
				dragon.worldObj.addWeatherEffect(entitybolt);

				boolean blnGriefing = dragon.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
				// こちらのcreateExplosion()はプレイヤーへのダメージ用。落雷でゲージ2.5個、爆発でゲージ2.5個分くらい削り取る。
				dragon.worldObj.createExplosion(dragon, x, y, z, 0.1F, true);
				// createExplosion()の第１引数をplayer2にすると、パーティクル表示は派手でもプレイヤーへのダメージはゼロになる。
				dragon.worldObj.createExplosion(player2, x, y, z, 2.0F, true);
			}
			// カウンターを10秒～15秒にリセット
			attackCounterLightningbolt = 300 + rand.nextInt(150);
			// 元は200+100だったが30tick/sのはず？
		}
		// 落雷攻撃コード・ここまで
	}

	// 機能しているエンダークリスタルの数（とりあえず初期値で100を格納）
	private static int countEnderCrystal = 100;

	/**
	 * 生き残ってるエンダークリスタルの数を取得
	 *
	 * @param dragon
	 */
	private int getCountEnderCrystal(EntityDragon dragon) {
		// 0になったら以後計算しない
		if (countEnderCrystal < 1) {
			return countEnderCrystal;
		}
		float dist1 = 256.0F;
		List list1 = dragon.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class,
				dragon.boundingBox.expand((double) dist1, (double) dist1, (double) dist1));
		countEnderCrystal = list1.size();

		return countEnderCrystal;
	}

}
