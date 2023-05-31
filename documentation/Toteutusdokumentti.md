# Toetutusdokumentti

- Ohjelma koostuu säännöllisten lausekkeiden tulkista ja tämän käyttöön tarkoitetusta yksinkertaisesta tekstikäyttöliittymästä.
- Tulkin toteutuksessa säännöllisen lausekkeen määrittely(symboli lauseke) muunnetaan merkkijonosta tietorakenteeksi.
- Muodostetun tietorakenteen avulla, voi sitten etsiä määritellyn kaltaisia merkkijonoja syötteenä annettavien merkkijonojen sisältä.
- Myöhemmin lisättiin toiminnallisuus vastaavien merkkijonojen etsimiseen myös tiedostoista.

## Tulkin kykyjen laajuus:
- Toteutus hallitsee RegEx operaatiot: *, +, ?, |, \\.
- Myös sulkujen ( ) käyttö toimii oikein.

## Tietorakenteen muodostaminen RegEx lausekkeesta:

1. RegEx lausekkeen pohjalta rakennetaan epädeterministinen äärellinen automaatti(NFA) kuvaamaan sitä.
2. NFA:sta muodostetaan deterministinen äärellinen automaatti(DFA).
3. DFA:ta optimoidaan poistamalla testien kannalta tarpeettomia tiloja. Näihin kuuluvat tilat jotka eivät johda mihinkään toiseen tilaan, eivätkä ole hyväksyviä tiloja.

Molemmat automaatti tyypit rakentuvat tiloista, joita kuvaa State class. State tyypin oliot voivat sisältää tyyppi muutoksia muuttujassa transitions, joissa tietty syöte merkki kuvautuu sitä seuraavaan tilaan.

## Tietorakenteen käyttö

Tulkki ottaa vastaan merkkijonon, jonka se käy läpi merkki kerrallaan, keräten säännöllistä lauseketta vastaavat merkkijonot sen sisältä. Se ei kerää alkuperäisessä merkkijonossa päällekkäin olevia merkkijonoja, vaan etenee sopivan merkkijonon löytäessään aina tämän merkkijonon jälkeiseen merkkiin. Lopuksi se palauttaa
listan, joka koostuu löydetyistä merkkijonoista.

## Toteutetut yleis tietorakenteet

Toteutettuja yleis tietorakenteita ovat LinkedDeque, CustomSet ja CustomMap.

LinkedDeque on Kaksisuuntaisen jonon toteutus, joka rakentuu kaksisuuntaisista lista solmuista. Valitsin tämän tietorakenteen siksi, että se auttaa erityisesti epädeterminististen automaattien muodostus prosessissa, kun automaatin tiloja voidaan koota jonoiksi, joiden kautta tarvittaviin jonon alku ja loppu osiin pääsee helposti käsiksi ja niiden oikeaa järjestystä on helppo ylläpitää. Tietorakenteesta on myös hyötyä, kun sitä käytetään pinona johon automaatin tiloista koostuvia jonoja voidaan sijoittaa odottamaan käsittelyä.

CustomSet on yksinkertainen joukko tietorakenteen toteutus taulukon avulla. Joukko on hyödyllinen tietorakenne determinististä äärellistä automaattia(DFA) muodostettaessa epädeterministisestä äärellisestä automaatista(NFA), koska DFA:t muodostetaan keräämällä NFA:n tiloja joukoiksi. Joukon ominaisuus, olla hyväksymättä duplikaatteja alkioistaan on myös hyödyllinen tässä prosessissa.

CustomMap on avain-arvo pareja hyödyntävä tietorakenne. Se hyödyntää CustomSet tietorakennetta avain-arvo parien ylläpidossa. Rakenne on hyödyllinen tulkin, toteutuksessa, koska se auttaa ylläpitämään ja hyödyntämään äärellisten automaattien tiloissa, niiden tilanmuutoksia eri syötteillä eri tiloihin.

## Saavutetut aikavaativuudet

RegEx määrittelyn mukaisten merkkijonojen keräämiseen merkkijonosta, jonka sisältämien merkkien määrä on n pitäisi pahimmassakin tapauksessa kulua aikaa enintään O(n^2) Todennäköisesti yleensä kuitenkin paljon vähemmän. Lähestyen minimiä O(n).

- DFA:n muodostaminen RegEx lausekkeesta, joka sisältää n merkkiä on aikavaativuudeltaan luokkaa O(n). Vakio kertoimet merkkien määrään verrattuna voivat kuitenkin nousta melko suuriksi käytännön toteutuksessa.

n kuvaa alkioiden määrää tietorakenteissa.

LinkedDeque:
- contains() toimii ajassa O(n)
- Kaikki muut käytön kannalta olennaiset operaatiot toimivat vakio ajassa O(1).

CustomSet:
- Koon haku size() toimii vakio ajassa O(1). contains(), add(), ja remove() ajassa O(n).
- Operaatiot jotka sisältävät toisia joukkoja, joissa on m alkiota aikavaativuus on O(n*m). Näitä ovat equals(), addAll(), removeAll(), containsAll().

CustomMap:
- Rakennettu luokan CustomSet varaan, joten aikavaativuudet ovat aikalailla samaa.

## Puutteet ja parannusehdotukset
- CustomMapin olisi voinut toteuttaa hajautustaulua hyödyntäen, jos aika olisi riittänyt.
- RegEx operaatioiden määrää olisi voinut lisätä enemmän ajan kanssa.

# Lähteet

Kehityksessä apuna on käytetty erityisesti näitä sivustoja:

http://www.codeproject.com/Articles/5412/Writing-own-regular-expression-parser

http://www.regexpal.com/