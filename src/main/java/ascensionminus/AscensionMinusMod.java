package ascensionminus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class AscensionMinusMod {
	public static final Logger logger = LogManager.getLogger(AscensionMinusMod.class.getName());
	public static final String MOD_ID = "AscensionMinus";

	// if we wanna get it to work with asc+ mod, we need a kind of interface to define max & min asc lvl
	public static final int MIN_ASC_LVL = -20;
	public static final int MAX_ASC_LVL = 20;

	public static void initialize() {
		new AscensionMinusMod();
	}
}
