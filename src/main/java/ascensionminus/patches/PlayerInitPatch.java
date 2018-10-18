package ascensionminus.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayerInitPatch {
	@SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
	public static class PlayerInit {
		@SpireInsertPatch(loc = 3089)
		public static void Insert() {
			if (AbstractDungeon.ascensionLevel <= -18) {
				AbstractDungeon.player.energy.energyMaster++;
			}
			if (AbstractDungeon.ascensionLevel <= -19) {
				AbstractDungeon.player.masterHandSize++;
			}
			if (AbstractDungeon.ascensionLevel <= -20) {
				for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
					c.upgrade();
			}
		}
	}
}
