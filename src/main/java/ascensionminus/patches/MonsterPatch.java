package ascensionminus.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;

public class MonsterPatch {

	@SpirePatch(clz = AbstractMonster.class, method = "init")
	public static class HealthPatch {
		public static void Postfix(AbstractMonster __instance) {
			if (AbstractDungeon.ascensionLevel <= -4 && __instance.type == EnemyType.NORMAL) {
				__instance.maxHealth = __instance.currentHealth = MathUtils.ceil(__instance.maxHealth * .85F);
			} else if (AbstractDungeon.ascensionLevel <= -5 && __instance.type == EnemyType.ELITE) {
				__instance.maxHealth = __instance.currentHealth = MathUtils.ceil(__instance.maxHealth * .85F);
			} else if (AbstractDungeon.ascensionLevel <= -6 && __instance.type == EnemyType.BOSS) {
				__instance.maxHealth = __instance.currentHealth = MathUtils.ceil(__instance.maxHealth * .85F);
			}
		}
	}

	@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { AbstractCreature.class, int.class,
			DamageType.class })
	public static class DamagePatch {
		public static void Postfix(DamageInfo __instance, AbstractCreature damageSource, int base, DamageType type) {
			if (damageSource instanceof AbstractMonster) {
				if (AbstractDungeon.ascensionLevel <= -1 && ((AbstractMonster) damageSource).type == EnemyType.NORMAL) {
					__instance.base = MathUtils.ceil(__instance.base * 0.75F);
					__instance.output = __instance.base;
				}
				if (AbstractDungeon.ascensionLevel <= -2 && ((AbstractMonster) damageSource).type == EnemyType.ELITE) {
					__instance.base = MathUtils.ceil(__instance.base * 0.75F);
					__instance.output = __instance.base;
				}
				if (AbstractDungeon.ascensionLevel <= -3 && ((AbstractMonster) damageSource).type == EnemyType.BOSS) {
					__instance.base = MathUtils.ceil(__instance.base * 0.75F);
					__instance.output = __instance.base;
				}
			}
		}
	}
}
