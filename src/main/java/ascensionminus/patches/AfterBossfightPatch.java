package ascensionminus.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public class AfterBossfightPatch {
	@SpirePatch(clz = AbstractRoom.class, method = "update")
	public static class RicherBosses {
		@SpireInsertPatch(rloc = 114, localvars = { "tmp" })
		public static void Insert(AbstractRoom __instance, @ByRef int[] tmp) {
			if (AbstractDungeon.ascensionLevel <= -8)
				tmp[0] = MathUtils.round(tmp[0] * 1.5F);
		}
	}

	@SpirePatch(clz = AbstractRoom.class, method = "update")
	public static class GainMaxHpAfterBoss {
		@SpireInsertPatch(rloc = 93)
		public static void Insert(AbstractRoom __instance) {
			if (__instance instanceof MonsterRoomBoss) {
				if (AbstractDungeon.ascensionLevel <= -11)
					AbstractDungeon.player.increaseMaxHp(AbstractDungeon.player.getAscensionMaxHPLoss(), true);
			}
		}
	}
}
