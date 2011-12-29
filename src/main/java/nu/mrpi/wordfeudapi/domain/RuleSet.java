package nu.mrpi.wordfeudapi.domain;

import java.util.Locale;

/**
 * @author Pierre Ingmansson
 */
public enum RuleSet {
    American(0, Locale.US),
    Norwegian(1, Locale.forLanguageTag("no")),
    Dutch(2, Locale.forLanguageTag("nl")),
    Danish(3, Locale.forLanguageTag("da")),
    Swedish(4, Locale.forLanguageTag("sv")),
    English(5),
    Spanish(6, Locale.forLanguageTag("es")),
    French(7, Locale.FRENCH),
    StrictSwedish(8, Locale.forLanguageTag("sv"));

    private final int apiIntRepresentation;
    private final Locale locale;

    RuleSet(final int apiIntRepresentation, final Locale locale) {
        this.apiIntRepresentation = apiIntRepresentation;
        this.locale = locale;
    }

    RuleSet(final int apiIntRepresentation) {
        this(apiIntRepresentation, Locale.ENGLISH);
    }

    public int getApiIntRepresentation() {
        return apiIntRepresentation;
    }

    public Locale getLocale() {
        return locale;
    }

    public static RuleSet fromByte(final byte number) {
        for (final RuleSet ruleSet : values()) {
            if (ruleSet.apiIntRepresentation == number) {
                return ruleSet;
            }
        }
        throw new IllegalArgumentException("RuleSet for value " + number + " not found!");
    }

    public static RuleSet fromString(final String string) {
        for (final RuleSet ruleSet : values()) {
            if (ruleSet.name().equalsIgnoreCase(string)) {
                return ruleSet;
            }
        }
        throw new IllegalArgumentException("RuleSet matching \"" + string + "\" not found!");
    }

    @Override
    public String toString() {
        return super.toString() + "(" + apiIntRepresentation + ")";
    }
}
