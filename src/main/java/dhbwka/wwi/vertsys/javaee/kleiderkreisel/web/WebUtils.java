/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.kleiderkreisel.web;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * Statische Hilfsmethoden
 */
public class WebUtils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Stellt sicher, dass einer URL der Kontextpfad der Anwendung vorangestellt
     * ist. Denn sonst ruft man aus Versehen Seiten auf, die nicht zur eigenen
     * Webanwendung gehören.
     *
     * @param request HttpRequest-Objekt
     * @param url Die aufzurufende URL
     * @return Die vollständige URL
     */
    public static String appUrl(HttpServletRequest request, String url) {
        return request.getContextPath() + url;
    }

    /**
     * Formatiert ein Datum für die Ausgabe, z.B. 31.12.9999
     *
     * @param date Datum
     * @return String für die Ausgabe
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Erzeugt ein Datumsobjekt aus dem übergebenen String, z.B. 03.06.1986
     *
     * @param input Eingegebener String
     * @return Datumsobjekt oder null bei einem Fehler
     */
    public static Date parseDate(String input) {
        try {
            java.util.Date date = DATE_FORMAT.parse(input);
            return new Date(date.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    public String formatDouble(Double d) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.GERMANY));
        return df.format(d);
    }
}
