# Testausdokumentti

## Oikean toiminnan testaus

Tulkin toiminnan testaus:
- Regex automaatin tilojen kartoitukseen tarkoitettu metodi käyttöliittymän yhteydessä. Avustaa ohjelman toiminnan tarkkailua.
- Erillaisten syötteiden kokeilu.
- Debuggaus viestien avulla ohjelman käyttäytyessä odottamattomasti.
- Toiminnan testaus JUnit testeillä.

LinkedDeque luokan toiminnallisuuden testaus:
- Luokan oikean toiminnan testaus JUnit testeillä.

CustomSet luokan toiminnallisuuden testaus:
- Luokan oikean toiminnan testaus JUnit testeillä.

CustomMap luokan toiminnallisuuden testaus:
- Luokan oikean toiminnan testaus JUnit testeillä.

## Ohjelman aikavaativuuksien testaus

Tulkin käyttöä testattiin neljällä RegEx lausekkeella joilla etsittiin osumia erikokoisista satunnais merkkijonoista.
- Katso [TulkinAikavaativuus.jpg](TulkinAikavaativuus.jpg) ja [PerformanceTesterLogs.md](PerformanceTesterLogs.md)

Toteutettuja tietorakenteita testattiin myös jonkin verran. Kaikille tietorakenteille tehtiin alkioiden lisäys testi ja jonkin muun operaation testi.
- Katso [PerformanceTesterLogs.md](PerformanceTesterLogs.md)