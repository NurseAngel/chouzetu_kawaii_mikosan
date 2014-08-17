package com.github.nurseangel.chouzetu_kawaii_mikosan;

import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntitySnowmanHook {

	/**
	 * スノーゴーレムの生まれをフック
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingSpawn(EntityJoinWorldEvent event)
	{
		if (!(event.entity instanceof EntitySnowman)) {
			return;
		}
		EntitySnowman snowGolem = (EntitySnowman) event.entity;
		// デフォルトのEntityAIArrowAttackを削除
		// XXX どうも正当な手段とは言い難い気がするのでもっとできればどうにか
		snowGolem.tasks.removeTask(((EntityAITasks.EntityAITaskEntry) snowGolem.tasks.taskEntries.get(0)).action);
		// 雪玉を連射
		snowGolem.tasks.addTask(1, new EntityAIArrowAttack(snowGolem, 1.25D, 1, 30.0F));
	}
}
