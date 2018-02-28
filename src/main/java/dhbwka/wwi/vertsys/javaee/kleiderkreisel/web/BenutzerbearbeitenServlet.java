package dhbwka.wwi.vertsys.javaee.kleiderkreisel.web;

/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.ejb.VerkaufsanzeigeBean;

import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.User;
import java.io.IOException;

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
@WebServlet(urlPatterns = "/app/bearbeiten/*")
public class BenutzerbearbeitenServlet extends HttpServlet {

    @EJB
    VerkaufsanzeigeBean verkaufsanzeigeBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Zu bearbeitende Aufgabe einlesen

        User user = this.userBean.getCurrentUser();
 
            request.setAttribute("task_form", this.createTaskForm(user));


        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/user_edit.jsp").forward(request, response);

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
                this.saveUser(request, response);
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
    private void saveUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String userUsername = request.getParameter("user_username");
        String userName = request.getParameter("user_name");
        String userAnschrift = request.getParameter("user_anschrift");
        String userPlz = request.getParameter("user_plz");
        String userOrt = request.getParameter("user_ort");
        String userTelefon = request.getParameter("user_telefon");
        String userEmail = request.getParameter("user_email");

        User user = this.userBean.getCurrentUser();

        /*if (dueDate != null) {
            task.setDueDate(dueDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }*/
        user.setUsername(userUsername);
        user.setName(userName);
        user.setAnschrift(userAnschrift);
        user.setPlz(userPlz);
        user.setOrt(userOrt);
        user.setTelefon(userTelefon);
        user.setEmail(userEmail);

        this.validationBean.validate(user, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.userBean.update(user);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeige/"));
        } else {
            response.sendRedirect(WebUtils.appUrl(request, "/app/bla/"));
            // Fehler: Formuler erneut anzeigen
            /*FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("task_form", formValues);

            response.sendRedirect(request.getRequestURI());*/
        }
    }

    

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param task Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createTaskForm(User user) {
        Map<String, String[]> values = new HashMap<>();

        values.put("user_username", new String[]{
            user.getUsername()
        });

        values.put("user_name", new String[]{
            user.getName()
        });

        values.put("user_anschrift", new String[]{
            user.getAnschrift()
        });

        values.put("user_plz", new String[]{
            user.getPlz()
        });

        values.put("user_ort", new String[]{
            user.getOrt()
        });

        values.put("user_telefon", new String[]{
            user.getTelefon()
        });
        
        values.put("user_email", new String[]{
            user.getEmail()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
