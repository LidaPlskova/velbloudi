<%@page contentType="text/html;charset=UTF-8" language="java"
%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"
%>
<!doctype html>



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
    <script>
        window.onload = init;

        function init() {
            var pole = document.querySelectorAll('input[type=range]');
            for (var i = 0; i < pole.length; i++) {
                pole[i].addEventListener('input', zmenaHodnoty);
            }
        }

        function zmenaHodnoty() {
            var vystup = document.querySelector('output[for=' + this.id + ']');
            vystup.innerText = this.value;
        }
    </script>
    <title>Výchozí webová stránka | Java 2 web </title>
</head>
<body>
    <div class="page">
        <form method="post">
            <h1>Hodnocení vašeho psa</h1>
            <h4>Jak je veliký?</h4>
            <div class="velikost">
                <input type="radio" name="velikost" value="2" checked="checked">Do ruky
                <input type="radio" name="velikost" value="4">Takový malý pejsek
                <input type="radio" name="velikost" value="3">Ani malý ani velký
                <input type="radio" name="velikost" value="2">Trošku větší
                <input type="radio" name="velikost" value="3">Halama
            </div>
            <br>
            <h4>Jaký má ocas?</h4>
            <div class="ocas">

                <label for="hunaty">
                    <input type="radio" name="ocas" value="2" class="s_obrazkem" id="hunaty" checked="checked">
                    <img src="${imagesLink}/obr1.jpg">
                </label>

                <label for="bez_ocasu">
                    <input type="radio" name="ocas" value="1" class="s_obrazkem" id="bez_ocasu">
                    <img src="${imagesLink}/obr2.jpg">
                </label>

                <label for="tenky">
                    <input type="radio" name="ocas" value="3" class="s_obrazkem" id="tenky">
                    <img src="${imagesLink}/obr3.jpg">
                </label>
            </div>
            <br>
            <h4>Jak dlouhou má srst?</h4>
            <div class="srst">
                <%--šoupátko--%>
                <output for="srst">5</output>
                cm
                <input type="range" id="srst" name="srst" min="0" max="30" value="5">
            </div>
            <br>
            <h4>Jaké má uši?</h4>
            <div class="usi">
                <%--roletka--%>
                <select name="usi">
                    <option value="1">Kupírované</option>
                    <option value="2">Špičaté</option>
                    <option value="3">Špičaté s překlopenou špičkou</option>
                    <option value="4">Dlouhé a klapaté (překlopené plachty)</option>
                </select>
            </div>
            <br>
            <h4>Kolik má let?</h4>
            <div class="roky">
                <%--numbers--%>
                <input type="number" name="roky" value="0" min="0">
            </div>
            <br>
            <input type="submit" value="Odeslat">
        </form>

    </div>
</body>
</html>
