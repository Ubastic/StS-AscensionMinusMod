package ascensionminus.patches;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import ascensionminus.AscensionMinusMod;

public class AscensionPatch {
	public static final Logger logger = LogManager.getLogger(AscensionPatch.class.getName());

	@SuppressWarnings({ "unchecked" })
	private static String[] AscensionLevels = ((Map<String, UIStrings>) new GsonBuilder().create().fromJson(
			Gdx.files.internal("localization/AscensionDescription.json").readString(String.valueOf(StandardCharsets.UTF_8)),
			new TypeToken<Map<String, UIStrings>>() {}.getType())).get("AscensionModeDescriptions").TEXT;
	private static int tmpAscLvl;
	private static boolean forcedUnlock = false;

	@SpirePatch(clz = CharacterOption.class, method = "incrementAscensionLevel")
	public static class IncrementPatch {
		public static SpireReturn<?> Prefix(CharacterOption __instance, @ByRef int[] level) {
			if (level[0] == 0) // Asc lvl can't be 0, if it's 0, it directly jump to 1.
				level[0] = 1;
			if (level[0] > 0) // only patch negative levels
				return SpireReturn.Continue();

			CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel = level[0];

			CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = AscensionLevels[-level[0] - 1];
			return SpireReturn.Return(null);
		}
	}

	@SpirePatch(clz = CharacterOption.class, method = "decrementAscensionLevel")
	public static class DecrementPatch {
		public static SpireReturn<?> Prefix(CharacterOption __instance, @ByRef int level[]) {
			if (level[0] == 0) // Asc lvl can't be 0, if it's 0, it directly jump to -1.
				level[0] = -1;
			if (level[0] > 0) // only patch negative levels
				return SpireReturn.Continue();

			if (level[0] < AscensionMinusMod.MIN_ASC_LVL) { // asc lvl can't be less then min
				level[0] = AscensionMinusMod.MIN_ASC_LVL;
			}

			CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel = level[0];

			CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = AscensionLevels[-level[0] - 1];
			return SpireReturn.Return(null);
		}
	}

	@SpirePatch(clz = CustomModeScreen.class, method = "renderAscension")
	public static class CustomRenderPatch {
		@SpireInsertPatch(rloc = 47)
		public static void Insert(CustomModeScreen __instance, SpriteBatch sb) {
			tmpAscLvl = __instance.ascensionLevel;
			if (__instance.ascensionLevel < 0)
				__instance.ascensionLevel = 0; // must set it to 0 or it will crash
		}

		public static void Postfix(CustomModeScreen __instance, SpriteBatch sb) {
			if (tmpAscLvl >= 0) // don't do anything if asc isn't negative
				return;

			if (tmpAscLvl < AscensionMinusMod.MIN_ASC_LVL) { // asc can't less than min
				tmpAscLvl = AscensionMinusMod.MIN_ASC_LVL;
			}

			__instance.ascensionLevel = tmpAscLvl;

			if (__instance.ascensionLevel != 0) {
				Field field = null;
				float screenX = 0;
				Hitbox ascensionModeHb = null;
				try {
					field = CustomModeScreen.class.getDeclaredField("screenX");
					field.setAccessible(true);
					screenX = (float) field.get(__instance);
					field = CustomModeScreen.class.getDeclaredField("ascensionModeHb");
					field.setAccessible(true);
					ascensionModeHb = (Hitbox) field.get(__instance);
				} catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
					e.printStackTrace();
					return;
				}

				FontHelper.renderSmartText(sb, FontHelper.charDescFont,
						CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = AscensionLevels[-__instance.ascensionLevel - 1],
						screenX + 475.0F * com.megacrit.cardcrawl.core.Settings.scale,
						ascensionModeHb.cY + 10.0F * com.megacrit.cardcrawl.core.Settings.scale, 9999.0F,
						32.0F * com.megacrit.cardcrawl.core.Settings.scale, com.megacrit.cardcrawl.core.Settings.CREAM_COLOR);
			}
		}
	}

	@SpirePatch(clz = TopPanel.class, method = "setupAscensionMode")
	public static class TopPanelPatch {
		@SuppressWarnings("rawtypes")
		@SpireInsertPatch(rloc = 5)
		public static SpireReturn Insert(TopPanel __instance) {
			if (AbstractDungeon.isAscensionMode && AbstractDungeon.ascensionLevel < 0) // only patch negative
				return SpireReturn.Return(null);
			return SpireReturn.Continue();
		}

		public static void Postfix(TopPanel __instance) {
			if (!AbstractDungeon.isAscensionMode || AbstractDungeon.ascensionLevel >= 0) // only patch negative
				return;
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < -AbstractDungeon.ascensionLevel; i++) {
				sb.append(AscensionLevels[i]);
				System.out.println(AscensionLevels[i]);
				if (i != -AbstractDungeon.ascensionLevel - 1) {
					sb.append(" NL ");
				}
			}
			Field ascensionStringF = null;
			try {
				ascensionStringF = TopPanel.class.getDeclaredField("ascensionString");
				ascensionStringF.setAccessible(true);
				ascensionStringF.set(__instance, sb.toString());
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@SpirePatch(clz = CustomModeScreen.class, method = "updateAscension")
	public static class CustomUpdatePatch {
		public static void Prefix(CustomModeScreen __instance) {
			// get all fields we need
			Field field = null;
			Hitbox ascLeftHb = null;
			Hitbox ascRightHb = null;
			try {
				field = CustomModeScreen.class.getDeclaredField("ascLeftHb");
				field.setAccessible(true);
				ascLeftHb = (Hitbox) field.get(__instance);

				field = CustomModeScreen.class.getDeclaredField("ascRightHb");
				field.setAccessible(true);
				ascRightHb = (Hitbox) field.get(__instance);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			// update hitboxes
			ascLeftHb.update();
			ascRightHb.update();
			// update asc lvl
			if ((ascLeftHb.clicked) || (CInputActionSet.pageLeftViewDeck.isJustPressed())) { // decrease asc
				CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F); // __instance.playClickFinishSound();
				ascLeftHb.clicked = false;
				__instance.ascensionLevel -= 1;
				if (__instance.ascensionLevel == 0) { // asc lvl can't be 0
					__instance.ascensionLevel = -1;
				}
				if (__instance.ascensionLevel < AscensionMinusMod.MIN_ASC_LVL) // the code in base game let asc lvl cycle from min to max
					__instance.ascensionLevel = AscensionMinusMod.MAX_ASC_LVL;
				__instance.isAscensionMode = true;
			} else if ((ascRightHb.clicked) || (CInputActionSet.pageRightViewExhaust.isJustPressed())) { // increase asc
				CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F); // __instance.playClickFinishSound();
				ascRightHb.clicked = false;
				__instance.ascensionLevel += 1;
				if (__instance.ascensionLevel == 0) { // asc lvl can't be 0
					__instance.ascensionLevel = 1;
				}
				if (__instance.ascensionLevel > AscensionMinusMod.MAX_ASC_LVL) { // the code in base game let asc lvl cycle from max to min
					__instance.ascensionLevel = AscensionMinusMod.MIN_ASC_LVL;
				}
				__instance.isAscensionMode = true;
			}
		}
	}

	@SpirePatch(clz = CharacterOption.class, method = "updateHitbox")
	public static class MaxAscLvlPatch {
		@SpireInsertPatch(rloc = 52)
		public static void Insert(CharacterOption __instance) {
			UnlockTracker.isAscensionUnlocked(__instance.c); // update forcedUnlock first
			if (!forcedUnlock) // only patch if forced unlock asc
				return;
			try {
				Field field = CharacterOption.class.getDeclaredField("maxAscensionLevel");
				field.setAccessible(true);
				field.setInt(__instance, -1); // set asc lvl to -1
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		public static void Postfix(CharacterOption __instance) {
			if (forcedUnlock && CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel > 0) // asc lvl can't > 0 if you didn't unlock
				__instance.decrementAscensionLevel(-1); // decrease asc lvl to -1
		}
	}

	@SpirePatch(clz = UnlockTracker.class, method = "isAscensionUnlocked")
	public static class ForceUnlockAscensionPatch1 { // if locked, set forceUnlock = true
		@SpireInsertPatch(loc = 678)
		public static SpireReturn<Boolean> Insert(AbstractPlayer p) {
			forcedUnlock = true;
			return SpireReturn.Return(true);
		}
	}

	@SpirePatch(clz = UnlockTracker.class, method = "isAscensionUnlocked")
	public static class ForceUnlockAscensionPatch2 { // if not locked, set forceUnlock = false
		@SpireInsertPatch(loc = 675)
		public static void Insert(AbstractPlayer p) {
			forcedUnlock = false;
		}
	}
}
