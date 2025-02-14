package utilities;

import java.util.regex.Pattern;

/**
 * Classe des patterns à utiliser pour les expressions régulières.
 */
public class Patterns {

    // Partie adresse

    public static final Pattern PATTERN_NUMERO_RUE = Pattern.compile("(?:\\d{0,3} +(bis|ter|quat)|\\G(?<!^))|(?:\\b\\d{0,3}(?:a|b)*\\b)");
    public static final Pattern PATTERN_NOM_RUE = Pattern.compile("\\b([a-zA" +
            "-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*" +
            "(?:[0-9]+)*([a-zA-Z\\u0080-\\u024F])*\\b");
    public static final Pattern PATTERN_CODE_POSTAL = Pattern.compile("\\b\\d{5}\\b");
    public static final Pattern PATTERN_VILLE = Pattern.compile("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b");


    // Partie société

    public static final Pattern PATTERN_TELEPHONE = Pattern.compile("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}");
    public static final Pattern PATTERN_MAIL = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
}
