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
        <c:choose>
            <c:when test="${edit}">
                Aufgabe bearbeiten
            </c:when>
            <c:otherwise>
                Aufgabe anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/anzeige_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/anzeige/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="anzeige_category">Kategorie:</label>
                <div class="side-by-side">
                    <select name="anzeige_category">
                        <option value="">Keine Kategorie</option>

                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}" ${anzeige_form.values["anzeige_category"][0] == category.id ? 'selected' : ''}>
                                <c:out value="${category.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <label for="anzeige_typ">Art des Angebots:</label>
                <div class="side-by-side">
                    <select name="anzeige_typ">
                        <option value="SUCHE">Suche</option>
                        <option value="BIETE">Biete</option>
                    </select>
                </div>

                <label for="anzeige_short_text">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="anzeige_short_text" value="${anzeige_form.values["anzeige_short_text"][0]}">
                </div>

                <label for="anzeige_long_text">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="anzeige_long_text"><c:out value="${anzeige_form.values['anzeige_long_text'][0]}"/></textarea>
                </div>
                
                <label for="anzeige_preis">Preis:</label>
                <div class="side-by-side">
                    <select name="anzeige_preisart">
                        <option value="VERHANDLUNGSBASIS">Verhandlungsbasis</option>
                        <option value="FESTPREIS">Festpreis</option>
                    </select>
                    <input type="text" name="anzeige_preis" value="${anzeige_form.values["anzeige_preis"][0]}">
                </div>

                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Sichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty anzeige_form.errors}">
                <ul class="errors">
                    <c:forEach items="${anzeige_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>