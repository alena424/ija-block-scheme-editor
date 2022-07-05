# IJA - projekt 2018
## Blokový editor


-----------------------------
Autoři:

Alena Tesařová (xtesar36)
Jan Šorm (xsormj00)

------------------------------


-----------------------
Popis blokového editoru
-----------------------
Blokový editor umí vytvářet, ukládat a načítat bloková schémata, kde každé schéma je složeno z bloků a propojů mezi nimi.
Každý blok má definované vstupní a výstupní porty. S každým portem je spojen typ, který je reprezentován množinou dat v podobě dvojic název=>hodnota (hodnota je vždy typu double).
Bloky je možné spojit pouze mezi výstupním a vstupním blokem. Každý blok obsahuje výpočet, který transformuje hodnoty ze vstupních portů na hodnoty výstupních portů. Je kontrolována komatibilita vstupního a výstupního propoje (musí být stejného typu). 
Výpočet je možné provést pouze tehdy, když jsou všechny bloky propojené. Nejsou možné cykly ve schématu.

----------
Typy portů
----------
Port1 - hodnoty: {Name1 : double}
Port2 - hodnoty: {Name2 : double} {Name3 : double}
Port3 - hodnoty: {Name4 : double} {Name5 : double} {Name6 : double}

----------
Typy bloků
----------
1. Addition 		IN: Port1, Port2, Port3 OUT: Port1
2. Invert			IN: Port1, Port2		OUT: Port1, Port2
3. Division			IN: Port1, Port2		OUT: Port2
4. Random			IN: Port1				OUT: Port1, Port2
5. Multiplication	IN: Port1, Port2		OUT: Port2

----------------
Výpočty - vzorce
----------------
1. Addition			OUT.Port1 = IN.Port1 + IN.Port2 + IN.Port3
2. Invert			OUT.Port1 = -IN.Port1 
					OUT.Port2 = -IN.Port2	
3. Division			OUT.Port2 = IN.Port2 / IN.Port1
4. Random			OUT.Port1 = IN.Port1 + rand(10)
					OUT.Port2 = IN.Port1 + rand(10)
5. Multiplication	OUT.Port2 = IN.Port2 * IN.Port1

--------
Ovládání
--------
Generování bloků:	kliknout levým tlačítkem myši na blok v levém panelu
Mazání bloků:		kliknout pravým tlačítkem myši na blok
Spojení bloků:		kliknout na port (zvýrazní se blok) a pak kliknout na druhý port, který chcete spojit
Zadávání hodnot: 	kliknutím pravým tlačítkem myši na spojení bloků, na levé straně se zobrazí tabulka na zadání hodnot, pro uložení hodnot, zmáčkněte tlačítko OK
Výpočet vzorce:		kliknout na tlačítko SOLVE, výsledek se objeví po najetí na spojení bloků
Nové schéma:		tlačítko NEW
Načíst schéma:		tlačítko OPEN
Uložit schéma:		tlačítko SAVE
Debbuging:			tlačíko DEBUG provede vždy jeden krok výpočtu
Zobrazení nápovědy:	tlačítko HELP zobrazí nápovědu


Poznámka: první bloky musí být spojené se START blokem a poslední bloky výpočtu musí být spojeny s END blokem, jinak schéma nepůjde sestavit

-------
Licence
-------
Zdrojové soubory je zakázáno šířit a libovolně upravovat, pouze se souhlasem autorů.
