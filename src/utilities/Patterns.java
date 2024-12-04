package utilities;

import java.util.regex.Pattern;

/**
 * Classe des patterns à utiliser pour les expressions régulières.
 */
public class Patterns {

    /**
     * Partie Adresse
     */
    public static final Pattern PATTERN_NUMERO_RUE = Pattern.compile("\\b\\d+\\w\\b");
    public static final Pattern PATTERN_NOM_RUE = Pattern.compile("\\D+\\w");
    public static final Pattern PATTERN_CODE_POSTAL = Pattern.compile("\\b\\d{5}\\b");
    public static final Pattern PATTERN_VILLE = Pattern.compile("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b");

    public static final Pattern PATTERN_TELEPHONE = Pattern.compile("(0[0-9]{9})");
    public static final Pattern PATTERN_MAIL = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
}
