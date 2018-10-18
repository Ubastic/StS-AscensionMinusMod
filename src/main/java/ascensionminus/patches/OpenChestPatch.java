package ascensionminus.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;

public class OpenChestPatch {
	@SpirePatch(clz = AbstractChest.class, method = "open")
	public static class OpenChest {
		public static void Prefix(AbstractChest __instance, boolean bossChest) {
			if (AbstractDungeon.ascensionLevel <= -20) {
				if (!bossChest)
					AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
			}
		}
	}
}
