<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Übersicht
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/anzeige_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/anzeige/new/"/>">Kleidungs-Anzeige anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/categories/"/>">Kategorien bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/app/bearbeiten/"/>">Benutzer bearbeiten</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_category">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}" ${param.search_category == category.id ? 'selected' : ''}>
                        <c:out value="${category.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_status">
                <option value="" ${param.search_status == '' ? 'selected' : ''}>Alle Angebotsarten</option>
                <option value="Biete" ${param.search_status == 'Biete' ? 'selected' : ''}>Biete</option>
                <option value="Suche" ${param.search_status == 'Suche' ? 'selected' : ''}>Suche</option>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty anzeige}">
                <p>
                    Es wurden keine Aufgaben gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.kleiderkreisel.web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Benutzer</th>
                            <th>Angebotsart</th>
                            <th>Preis</th>
                            <th>Preistyp</th>
                            <th>Datum</th>
                        </tr>
                    </thead>
                    <c:forEach items="${anzeige}" var="anzeige">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/anzeige/${anzeige.id}/"/>">
                                    <c:out value="${anzeige.shortText}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${anzeige.category.name}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.owner.username}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.anzeigeArt.label}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.preis}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.preisArt.label}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(anzeige.dueDate)}"/>
                              
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>