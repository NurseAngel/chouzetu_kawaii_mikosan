package com.github.nurseangel.chouzetu_kawaii_mikosan;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config {

	/**
	 * コンストラクタ
	 *
	 * @param cfg
	 * @return
	 */
	public Config(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		readConfig(cfg);
	}

	public static boolean isTest = false;

	public static boolean enderdragon;
	public static boolean snowgolel;

	/**
	 * コンフィグファイルから読み込み
	 *
	 * @param cfg
	 */
	private void readConfig(Configuration cfg) {

		String comment1 = "Enderdragon spits flames.";
		String comment2 = "Snowgolem fire snowbolls.";

		try {
			cfg.load();
			enderdragon = cfg.get(Configuration.CATEGORY_GENERAL, "enderdragon", true, comment1).getBoolean(true);
			snowgolel = cfg.get(Configuration.CATEGORY_GENERAL, "snowgolel", true, comment2).getBoolean(true);
		} catch (Exception e) {
			FMLLog.log(Level.INFO, Reference.MOD_NAME + " configuration loadding failed");
		} finally {
			cfg.save();
		}
	}

}
