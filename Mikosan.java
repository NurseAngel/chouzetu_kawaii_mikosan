package com.github.nurseangel.chouzetu_kawaii_mikosan;

import net.minecraftforge.common.MinecraftForge;

import com.github.nurseangel.chouzetu_kawaii_mikosan.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Mikosan {
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	@Mod.Instance(Reference.MOD_ID)
	public static Mikosan instance;

	public static Config config;

	/**
	 * コンストラクタ的なもの
	 *
	 * @param event
	 */
	@Mod.EventHandler
	public void modPreInit(FMLPreInitializationEvent event) {
		config = new Config(event);
	}

	/**
	 * メイン処理的なもの
	 *
	 * @param event
	 */
	@Mod.EventHandler
	public void modInit(FMLInitializationEvent event) {
		// スノーゴーレム
		if (config.snowgolem) {
			addSnowGolem();
		}
		// エンダードラゴン
		if (config.enderdragon) {
			addEnderdragon();
		}
	}

	private void addSnowGolem() {
		// スノーゴーレムの連射速度を1に
		MinecraftForge.EVENT_BUS.register(new EntitySnowmanHook());

	}

	private void addEnderdragon() {
		MinecraftForge.EVENT_BUS.register(new EntityEnderdragonHook());
	}

}
