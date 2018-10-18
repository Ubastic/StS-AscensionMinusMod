package ascensionminus.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GainMoreGoldPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "gainGold")
	public static class GainMoreGold {
		public static void Prefix(AbstractPlayer __instance, @ByRef int[] amount) {
			if (AbstractDungeon.ascensionLevel <= -12)
				amount[0] = MathUtils.round(amount[0] * 1.2F);
		}
	}
}
