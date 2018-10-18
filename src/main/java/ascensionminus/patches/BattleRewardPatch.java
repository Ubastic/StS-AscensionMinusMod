package ascensionminus.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BattleRewardPatch {
	@SpirePatch(clz = AbstractRoom.class, method = "update")
	public static class NormalEnemiesDropRelic {
		@SpireInsertPatch(rloc = 153)
		public static void Insert(AbstractRoom __instance) {
			if (AbstractDungeon.ascensionLevel <= -14)
				if (AbstractDungeon.potionRng.random(0, 99) < 20)
					__instance.addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
		}
	}

	@SpirePatch(clz = AbstractRoom.class, method = "update")
	public static class ElitesDropCard {
		@SpireInsertPatch(rloc = 142)
		public static void Insert(AbstractRoom __instance) {
			if (AbstractDungeon.ascensionLevel <= -15)
				__instance.addCardToRewards();
		}
	}

	@SpirePatch(clz = AbstractRoom.class, method = "update")
	public static class BossDropRelic {
		@SpireInsertPatch(rloc = 104)
		public static void Insert(AbstractRoom __instance) {
			if (AbstractDungeon.ascensionLevel <= -16)
				__instance.addRelicToRewards(AbstractDungeon.returnRandomRelic(RelicTier.RARE));
		}
	}

	@SpirePatch(clz = AbstractRoom.class, method = "update")
	public static class RewardsContainPotion {
		@SpireInsertPatch(rloc = 167)
		public static void Insert(AbstractRoom __instance) {
			if (AbstractDungeon.ascensionLevel <= -17)
				__instance.rewards.add(new RewardItem(AbstractDungeon.returnRandomPotion()));
		}
	}
}
