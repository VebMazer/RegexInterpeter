# Käyttöohje

## RegEx toiminnan laajuus

Toeteutettu tulkki hallitsee RegEx symbolit: *,+,?,|,\
ja tavallisten sulkujen ( ) oikean käytön. Muita merkkejä
kohdellaan tavanomaisena syötteenä.

Poikkeuksena kuitenkin symboli ¤ joka on toistaiseksi varattu concat operaatiolle ja 
sitä ei tulisi käyttää syöte merkkijonoissa, eikä lausekkeiden määrittelyissä. Koodin
pitäisi pääosin kohdella sitä, kuin paikassa ei olisi mitään, mutta poikkeuksia saatta
olla läsnä.

## Toiminnan testausta varten.

Ohjelmaa käytetään tekstikäyttöliittymän kautta. Sen ydin toiminnan vertailukohteena
voi käyttää sivustoa: http://www.regexpal.com/
- Muistaen kuitenkin, että regexpal ymmärtää paljon laajemman joukon RegEx symboleita,
joista osaa toteutettu tulkki kohtelee vain tavanomaisina merkkeinä. Jos näitä ei satu 
muistamaan niin testailun pitäisi toimia oikein ainakin niin kauan, kun pysytään yllä 
mainituissa symboleissa ja alphanumeerisissa merkeissä syötteenä.
- Tämä siis niin, että määritellän RegEx lauseke(vaihtoehto 1) ja sitten testataan 
merkkijonoja(vaihtoehto 2). Jos RegEx lausekkeen määrittely on sama, kuin sivulla
regexpal ja sivun kenttään Test String käytetään samaa merkkijonoa, kuin ohjelman
testauksessa(vaihtoehto 2), niin tulisi ohjelman palauttaa samat merkkijonot, jotka
regexpal rajaa testimerkkijonosta hyväksytyiksi.

## Tekstikäyttöliittymän käyttö ja toiminnallisuus

Ohjelma pyytää käyttäjää valitsemaan listaamistaan toiminto vaihtoehdoista syöttämällä
haluamansa vaihtoehdon numeron.

Käyttöliittymässä on kolme eri komento looppia, joiden suoritus etenee seuraavasti:

 Aloituslooppi, jossa käyttäjä voi valita vaihtoehdoista 1 ja 4: 
- 1: Pyytää käyttäjää määrittelemään säännöllisen lausekkeen ja siirtyy senjälkeen 
ohjelman päälooppiin.
- 4: Poistuu ohjelmasta.

Päälooppi sisältää toiminto vaihtoehdot 1 ja 4, jotka toimivat samalla tavoin, kuin 
aloitusloopissa, sekä vaihtoehdot 2 ja 3:
- 2: Pyytää käyttäjää syöttämään merkkijonon, josta kerätään viimeiseksi 
määritellyn säännöllisen lausekkeen mukaiset merkkijonot. Sopivan merkkijonon 
löytymisen jälkeen edetään etsinnässä löydetyn merkkijonon viimeistä merkkiä seuraavaan 
merkkiin, joten alkuperäisessä merkkijonossa päällekkäin olevia merkkijonoja ei palauteta.
- 3: siirtää käyttäjän debug looppiin.

(Devaajalle tarkoitettu looppi, joka on ohjelman ydin toiminnan kannalta tarpeeton.)
Debugloopin toiminto vaihtoehdot(5-9) tarjoavat toisen testaus työkalun ja tietoa
määritellyn RegEx lausekkeen pohjalta rakennetuista tietorakenteista. Tämän loopin
käyttö ei kuitenkaan ole tarpeellista käyttäjän kannalta, vaan tarjoaa lähinnä lisätyökaluja
devaajalle ohjelman toiminnan analysointiin.
- 5: Pyytää käyttäjältä merkkijonon ja palauttaa true, jos merkkijonon merkkien
mukainen järjestys kulkee DFA:n sisällä, muuten palauttaa false. Toiminto ei siis tarkasta
onko tila johon automaatin sisällä ollaan päädytty merkkijonon lopussa hyväksyvä tila, ja
palauttaa false, vaikka hyväksyvässä tilassa oltaisiin käyty jos siitä on sen jälkeen liikuttu, 
tai yritetty liikkua pois.
- 6: Palauttaa listan DFA:n tiloista, tarjoten jokaista tilaa kohden tiedon
siitä jos tila on hyväksyvä tila, sekä tilan tunnistenumeron ja tiedon siitä mihin tiloihin
tilasta pääse milläkin syöttellä, jos tilasta siis voi edetä toisiin tiloihin.
Vaihtoehto
- 7: Näyttää RegEx lausekkeesta muokatun version, joka sisältää merkit concat 
operaatiota varten, NFA:n muodostuksessa.
- 8: Tulostaa listan 5 toiminnolla tehtyjen testien tuloksista.
- 9: Palaa käyttöliittymän päälooppiin.

