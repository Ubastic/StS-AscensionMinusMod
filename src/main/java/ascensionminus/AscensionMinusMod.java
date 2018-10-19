package ascensionminus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import basemod.BaseMod;
import basemod.interfaces.ISubscriber;

@SpireInitializer
public class AscensionMinusMod implements ISubscriber {
	public static final Logger logger = LogManager.getLogger(AscensionMinusMod.class.getName());
	public static final String MOD_ID = "AscensionMinus";
	private static final String MOD_NAME = "Ascension -";
	private static final String AUTHOR = "yhrcyt";
	private static final String DESCRIPTION = "Make your game easier!";

	// if we wanna get it to work with asc+ mod, we need a kind of interface to define max & min asc lvl
	public static final int MIN_ASC_LVL = -20;
	public static final int MAX_ASC_LVL = 20;

	public AscensionMinusMod() {
		BaseMod.subscribe(this);
	}

	public static void initialize() {
		new AscensionMinusMod();
	}

	public void receivePostInitialize() {
		Texture badgeTexture = ImageMaster.loadImage("images/badge.png");
		BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, null);
	}

	// -------------------------- ASC CHANGES --------------------------

}
