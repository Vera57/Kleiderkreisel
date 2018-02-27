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

import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.VerkaufsanzeigeBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.PreisArt;
//import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.User;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.Verkaufsanzeige;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/anzeige/*")
public class VerkaufsanzeigeEditServlet extends HttpServlet {

    @EJB
    VerkaufsanzeigeBean VerkaufsanzeigeBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Verkaufsanzeige anzeige = this.getRequestedAnzeige(request);
        request.setAttribute("edit", anzeige.getId() != 0);
                                
        /*if (session.getAttribute("anzeige_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("anzeige_form", this.createAnzeigeForm(anzeige));
        }*/

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/anzeige_edit.jsp").forward(request, response);

        session.removeAttribute("anzeige_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                this.saveAnzeige(request, response);
                break;
            case "delete":
                this.deleteAnzeige(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveAnzeige(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();
        
        String anzeigeErsteller=request.getParameter("anzeige_ersteller");
        String anzeigeCategory = request.getParameter("anzeige_category");
        String anzeigeArt=request.getParameter("anzeige_Art");
        String anzeigehortText = request.getParameter("anzeige_short_text");
        String anzeigeLongText = request.getParameter("anzeige_long_text");
        String anzeigeDueDate = request.getParameter("anzeige_due_date");
        String anzeigePreis=request.getParameter("anzeigePreis");
        String anzeigePreisArt=request.getParameter("anzeige_PreisArt");
        
                
        Verkaufsanzeige anzeige = this.getRequestedAnzeige(request);
        
        try {
            anzeige.getOwner().getUsername();
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Benutzer ist nicht vorhanden.");
        }

        if (anzeigeCategory != null && !anzeigeCategory.trim().isEmpty()) {
            try {
                anzeige.setCategory(this.categoryBean.findById(Long.parseLong(anzeigeCategory)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dueDate = WebUtils.parseDate(anzeigeDueDate);
       

        if (dueDate != null) {
            anzeige.setDueDate(dueDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        anzeige.setShortText(anzeigehortText);
        anzeige.setLongText(anzeigeLongText);
        
        long preis=new Long(anzeigePreis);
        if (anzeigePreis!=null){
            anzeige.setPreis(preis);
        }
        
        try {
            anzeige.setPreisArt(PreisArt.valueOf(anzeigePreisArt));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Art des Preises ist nicht vorhanden.");
        }
        
        try {
            anzeige.setAnzeigeArt(AnzeigeArt.valueOf(anzeigeArt));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Art der Anzeige ist nicht vorhanden.");
        }
        this.validationBean.validate(anzeige, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.VerkaufsanzeigeBean.update(anzeige);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeige/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("anzeige_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteAnzeige(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Verkaufsanzeige anzeige = this.getRequestedAnzeige(request);
        this.VerkaufsanzeigeBean.delete(anzeige);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/anzeige/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Verkaufsanzeige getRequestedAnzeige(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Verkaufsanzeige anzeige = new Verkaufsanzeige();
        anzeige.setOwner(this.userBean.getCurrentUser());
        anzeige.setDueDate(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String anzeigeId = request.getPathInfo();

        if (anzeigeId == null) {
            anzeigeId = "";
        }

        anzeigeId = anzeigeId.substring(1);

        if (anzeigeId.endsWith("/")) {
            anzeigeId = anzeigeId.substring(0, anzeigeId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            anzeige = this.VerkaufsanzeigeBean.findById(Long.parseLong(anzeigeId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return anzeige;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param anzeige Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createAnzeigeForm(Verkaufsanzeige anzeige) {
        Map<String, String[]> values = new HashMap<>();

        values.put("anzeige_owner", new String[]{
            anzeige.getOwner().getUsername()
        });

        if (anzeige.getCategory() != null) {
            values.put("anzeige_category", new String[]{
                anzeige.getCategory().toString()
            });
        }
        
      /*

        values.put("anzeige_short_text", new String[]{
            anzeige.getShortText()
        });*/

        values.put("anzeige_long_text", new String[]{
            anzeige.getLongText()
        });
        
        values.put("anzeige_due_date", new String[]{
            WebUtils.formatDate(anzeige.getDueDate())
        });
        
        values.put("anzeige_preis", new String[]{
            Long.toString(anzeige.getPreis())
        });
        
        values.put("anzeige_preisArt",new String[]{
            anzeige.getPreisArt().toString()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
