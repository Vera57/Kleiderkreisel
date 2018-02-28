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
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.Category;
import dhbwka.wwi.vertsys.javaee.kleiderkreisel.jpa.Verkaufsanzeige;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die Startseite bzw. jede Seite, die eine Liste der Anzeige
 * zeigt.
 */
@WebServlet(urlPatterns = {"/app/anzeige/"})
public class VerkaufsanzeigeListServlet extends HttpServlet {

    @EJB
    private CategoryBean categoryBean;
    
    @EJB
    private VerkaufsanzeigeBean VerkaufsanzeigeBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        
        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchCategory = request.getParameter("search_category");
        String searchStatus = request.getParameter("search_status");

        // Anzuzeigende Aufgaben suchen
        Category category = null;

        if (searchCategory != null) {
            try {
                category = this.categoryBean.findById(Long.parseLong(searchCategory));
            } catch (NumberFormatException ex) {
                category = null;
            }
        }

        List<Verkaufsanzeige> anzeige = this.VerkaufsanzeigeBean.search(searchText, category, searchStatus);
        request.setAttribute("anzeige", anzeige);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/anzeige_list.jsp").forward(request, response);
    }
}
