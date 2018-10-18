package ascensionminus.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;

public class ShopCostLessPatch {
	@SpirePatch(clz = ShopScreen.class, method = "init")
	public static class ShopCostLess {
		public static void Postfix(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
			if (AbstractDungeon.ascensionLevel <= -10) {
				__instance.applyDiscount(0.75F, true);
			}
		}
	}
}
