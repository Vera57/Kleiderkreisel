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
        Benutzer bearbeiten
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/user_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/anzeige/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

   <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    <%-- Eingabefelder --%>
                    <h1>Logindaten ändern</h1>
                    <label for="user_username">
                        Benutzername:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_username" value="${task_form.values["user_username"][0]}">
                    </div>
                    
                    <h1>Anschrift ändern</h1>
                    
                    <label for="user_name">
                        Vorname und Nachname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_name" value="${task_form.values["user_name"][0]}">
                    </div>
                    
                    <label for="user_anschrift">
                        Straße und Hausnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_anschrift" value="${task_form.values["user_anschrift"][0]}">
                    </div>
                    
                    <label for="user_plz">
                        Postleitzahl:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_plz" value="${task_form.values["user_plz"][0]}">
                    </div>
                    
                    <label for="user_ort">
                        Ort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_ort" value="${task_form.values["user_ort"][0]}">
                    </div>
                    
                    <h1>Kontaktdaten ändern</h1>
                    
                    <label for="user_telefon">
                        Telefonnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_telefon" value="${task_form.values["user_telefon"][0]}">
                    </div>
                    
                    <label for="user_email">
                        E-Mailadresse:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_email" value="${task_form.values["user_email"][0]}">
                    </div>

                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button class="icon-pencil" type="submit" value="save" name="action">
                            Änderungen speichern
                        </button>
                    </div>
                </div>

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty task_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${task_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
        </div>
    </jsp:attribute>
</template:base>