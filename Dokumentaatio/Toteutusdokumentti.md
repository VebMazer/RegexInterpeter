Ohjelma koostuu säännöllisten lausekkeiden tulkista ja tämän käyttöön
tarkoitetusta yksinkertaisesta tekstikäyttöliittymästä.

Tulkin toteutuksessa säännöllisen lausekkeen määrittely muunnetaan
merkkijonosta tietorakenteeksi.

Ensin merkkijono muunnetaan epädeterminisitiseksi äärelliseksi automaatiksi(NFA), joka
taas muunnetaan tämän jälkeen deterministiseksi äärelliseksi automaatiksi(DFA).
Sen jälkeen tätä automaattia vielä optimoidaan poistamalla siitä tarpeettomia osia.

Tulkkia käytettäessä se vertailee sille syötettyjä merkkijonoja tähän automaattiin
ja palauttaa true, jos vertailtava merkkijono kuuluu säännöllisen lausekkeen määrittelemään
ryhmään.

Molemmat automaatti tyypit rakentuvat tiloista, joita kuvaa State class. State tyypin
oliot voivat sisältää tyyppi muutoksia muuttujassa transitions, joissa tietty syöte merkki 
kuvautuu sitä seuraavaan tilaan.