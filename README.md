# AI-Projekts_un_Mobilas_Aplikacijas
Apraksts:
video links:

Uzstādīšana pa soļiem:
    1) Lejuplādējam Android Studio
    2) Lejuplādējam XAMPP lokālo serveri
    3) Lejuplādējam mariadb serveri https://mariadb.org/download/?t=mariadb&p=mariadb&r=11.7.2&os=windows&cpu=x86_64&pkg=msi&mirror=uab
    4) Kad "XAMPP" uzinstelēts palaižam Apache un MySQL
    5) Kad "XAMPP" palaists dodamies uz http://localhost/dashboard/
    6) Nospiežam pogu "phpMyAdmin"
    7) Spiežam Import un ievietojam "antiscamdb.sql"
    8) Kad datubāze uzstādīta dodamiem "\xampp\htdocs"
    9) Ievietojam mapi ar nosaukumu "asca_server"
    10) Atveram Android Studio spiežam "open" un norādam AI-Projekts_un_Mobilas_Aplikacijas\asca
    11) Atveram kotlin+java/com.kstraupenieks.asca/Constants
    12) Atveram CMD ievadam "ipconfig" meklējam sadaļu Ethernet adapter Ethernet: un kopejam IPv4 Address
    13) Failā Constants sadaļā BASE_URL = "http:// Tava IPV4 adrese/asca_server/ "
    14) Sadaļā API_KEY="" vadam savu openAI API KEY
    15) Pieslēdzam telefonu ar USB vadu pie datora
    16) Telefonam ieslēdzam "developer mode"
    17) Dodamies uz Settings -> Developer options -> ieslēdzam USB Debugging
    18) Atslēdzam mobīlos datus un pieslēdzamies vienā tīklā ar datoru uz kuru ir uzstādīts XAMPP serveris 
    19) Android studio nospiežam -> run "app"
    20) Testējam
    