# Aihemäärittely

Säännöllisten lausekkeiden(RegEx) tulkki, joka toteutetaan javalla.

Tulkille määritellään RegEx symboli lauseke ja sitten sille voi syöttää merkkijonoja, jotka
se käy läpi merkki kerrallaan keräten lausekkeen hyväksymät merkkijonot sen sisältä
kuitenkin niin, että päällekkäisiä merkkijonoja ei kerätä, koska sopivan merkkijonon
löytyessä siirrytään merkkijonoa seuraavaan merkkiin. Lopuksi palautetaan joukko, 
joka koostuu kerätyistä merkkijonoista.

- Aiheen olennaisin ongelma on, säännöllisten lausekkeiden(RegEx) kuvaaminen 
tietorakenteina, joita voidaan vertailla helposti ja tehokkaasti merkkijonoihin.
- Tulkin toteutuksen ydin perustuu RegEx lausekkeiden pohjalta rakennettaviin äärellisiin
automaatteihin.

## Tietorakenteen muodostaminen RegEx lausekkeesta:
1. RegEx lausekkeen pohjalta rakennetaan epädeterministinen äärellinen 
automaatti(NFA) kuvaamaan sitä.
2. NFA:sta muodostetaan deterministinen äärellinen automaatti(DFA).
3. DFA:ta optimoidaan poistamalla testien kannalta tarpeettomia tiloja poistamalla.

## Tulkkia tukevat tietorakenteet
1. Kaksisuuntainen jono.(Deque)
- NFA tilojen kokoamiseen ja käsittelyyn.
2. Joukko, joka ei siis salli duplikaatteja.(Set)
- DFA tilojen muodostamiseen NFA tiloista
3. Avain-arvo pareja tukeva tietorakenne.(Map)
- Tilan muutosten kuvaamiseen NFA ja DFA tiloissa.

## Tietorakenteen käyttö:
Tulkki ottaa vastaan merkkijonon, jonka se käy läpi merkki kerrallaan, keräten
säännöllistä lauseketta vastaavat merkkijonot sen sisältä. Se ei kerää alkuperäisessä 
merkkijonossa päällekkäin olevia merkkijonoja, vaan etenee sopivan merkkijonon 
löytäessään aina tämän merkkijonon jälkeiseen merkkiin. Lopuksi se palauttaa
listan, joka koostuu löydetyistä merkkijonoista.

## Aikavaativuudet:
- RegEx määrittelyn mukaisten merkkijonojen keräämiseen merkkijonosta, jonka sisältämien
merkkien määrä on n pitäisi pahimmassakin tapauksessa kulua aikaa enintään O(n^2) 
Todennäköisesti yleensä kuitenkin paljon vähemmän. Lähestyen minimiä O(n).
- DFA:n muodostaminen RegEx lausekkeesta, joka sisältää n merkkiä on aikavaativuudeltaan 
perjaatteessa luokkaa O(n). Vakio kertoimet merkkien määrään verrattuna voivat 
kuitenkin nousta melko suuriksi käytännön toteutuksessa.
