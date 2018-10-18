package ascensionminus.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HigherMaxHpPatch {
	@SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
	public static class HigherMaxHp {
		@SpireInsertPatch(loc = 3089)
		public static void Insert() {
			if (AbstractDungeon.ascensionLevel <= -9)
				AbstractDungeon.player.increaseMaxHp(AbstractDungeon.player.getAscensionMaxHPLoss(), false);
		}
	}
}
