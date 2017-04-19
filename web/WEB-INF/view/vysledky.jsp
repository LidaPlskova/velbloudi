<%@page contentType="text/html;charset=UTF-8" language="java"
%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"
%>
<!doctype html>


<jsp:useBean id="vysledek"
             type="java.lang.Integer"
             scope="request"/>
<jstl:url var="cssLink" value="/css/styly.css"/>    <%-- css setting in any subpage - use ${cssLink} --%>
<jstl:url var="rootLink" value="/"/>                <%-- back to HP from anywhere - use ${rootLink}  --%>
<jstl:url var="imagesLink"
          value="/images"/>        <%-- images dir setting in any subpage - use ${imagesLink}  --%>

<html lang="cs">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${cssLink}">
    <link rel="icon" href="${imagesLink}/favicon.png">
    <title>Výchozí webová stránka | Java 2 web </title>
</head>
<body>
    <div class="page">
        <h1>Hodnocení vašeho psa</h1>
        <h3>Váš psí miláček získal ${vysledek} bodů z ... to je stejně jedno, protože ho máte rádi v každém případě.</h3>

</body>
</html>
