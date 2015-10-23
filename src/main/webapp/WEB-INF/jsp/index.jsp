<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Interactive character sheet">
        <meta name="keywords" content="DnD, D&D, Character Sheet">
        <title>Chronicler</title>

        <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
        <link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
        <script src="<c:url value="/resources/js/jquery-1.11.3.min.js" />"></script>
        <script src="<c:url value="/resources/js/tabSwitcher.js" />"></script>
        <script src="<c:url value="/resources/js/dashboard.js" />"></script>
    </head>

    <header>
        <h1>${name}</h1>
        <h3>Level ${level}</h3>
        <h3>${raceID}+' '+${classID}</h3>
    </header>

    <body>

        <div class="dashboard closed">
            <span id="menuButton"><h2>|||</h2></span>
            <nav class="dashboardContent">
                <ul>
                    <li><a>My Campaigns</a></li>
                    <li><a>My Character</a></li>
                    <li><a>My account</a></li>
                    <li><a>About this page</a></li>
                </ul>
            </nav>
        </div>


        <div class="container">
            <ul class="tabs">
                <li class="tab selected"><a href='#combatTab'>Combat</a></li>
                <li class="tab" ><a href='#spellTab'>Spells</a></li>
                <li class="tab" ><a href='#featTab'>Feats</a></li>
                <li class="tab"><a href='#skillTab'>Skills</a></li>
                <li class="tab"><a href='#inventoryTab'>Inventory</a></li>
                <li class="tab"><a href='#aboutTab'>About character</a></li>
            </ul>

            <div class="sheetContent">
                <!-- this is where we put includes and hide/show depending on tab -->
                <div id="combatTab"><jsp:include page="cs_combat.jsp" /></div>
                <div id="spellTab"><jsp:include page="cs_spells.jsp"/></div>
                <div id="featTab"><jsp:include page="cs_feats.jsp"/></div>
                <div id="skillTab"><jsp:include page="cs_skills.jsp"/></div>
                <div id="inventoryTab"><jsp:include page="cs_inventory.jsp"/></div>
                <div id="aboutTab"><jsp:include page="cs_about.jsp"/></div>
            </div>
        </div>
    </body>

    <footer>
    </footer>

</html>