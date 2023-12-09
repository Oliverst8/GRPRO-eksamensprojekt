# GRPRO - Eksamensprojekt

## Fortolkning af krav

### Uge 1

#### K1-1a | Græs kan blive plantet når input filerne beskriver de&e. Græs skal blot tilflfældigt placeres.

- Græs skal placeres tilfældigt på kortet når input filerne beskriver det.

#### K1-1b | Græs kan nedbrydes og forsvinde.

- Over tid skal græs nedbrydes og forsvinde.

#### K1-1c | Græs kan sprede sig.

- Græs skal kunne sprede sig til tilstødende felter.

#### K1-1d | Dyr kan stå på græs uden der sker noget med græsset (Her kan interfacet NonBlocking udnyttes).

- Dyr skal kunne stå på græs uden at græsset bliver påvirket.

#### K1-2a | Kaniner kan placeres på kortet når input filerne beskriver de&e. Kaniner skal blot tilfældigt placeres.

- Kaniner skal placeres tilfældigt på kortet når input filerne beskriver det.

#### k1-2b | Kaniner kan dø, hvilket resulterer I at de fjernes fra verdenen.

- Kaniner skal kunne dø og dermed blive fjernet fra verdenen.

#### K1-2c | Kaniner lever af græs som de spiser i løbet af dagen, uden mad dør en kanin.

- Kaniner skal kunne spise græs og derved overleve.
- Kaniner skal kunne dø af sult.

#### k1-2d | Kaniners alder bestemmer hvor meget energi de har

- Jo ældre en kanin er, desto mindre energi kan den have.

#### K1-2e | Kaniner kan reproducere.

- Kaniner skal kunne reproducere sig.
- Kaniner skal være i deres huller når de reproducere sig.

#### k1-2f | Kaniner kan grave huller, eller dele eksisterende huller med andre kaniner. Kaniner kan kun være tilknyttet ét hul.

- Kaniner skal kunne grave hulsystemer.
- Flere kaniner skal kunne dele samme hulsystem.
- En kanin skal kun kunne være tilknyttet ét hulsystem.

#### K1-2g | Kaniner søger mod deres huller når det bliver after, hvor de sover.

- Kaniner skal søge mod deres hulsystem når det bliver aften.
- Kaniner skal sove i deres hulsystem.

#### K1-3a | Huller kan enten blive indsat når input filerne beskriver dette, eller graves af kaniner. Huller skal blot blive tilfældigt placeret når de indgår i en input fil.

- En indgang til et hulsystem skal kunne placeres tilfældigt på kortet når input filerne beskriver det.
- Kaniner skal kunne grave hulsystemer.

#### K1-3b | Dyr kan stå på et kaninhul uden der sker noget.

- Dyr skal kunne stå på et kaninhul uden at hullet eller dyren bliver påvirket.

#### KF1-1 (Frivilligt) | Huller består altid minimum af en indgang, der kan dog være flere indgange som sammen former én kanin tunnel. Kaniner kan kun grave nye udgange mens de er i deres huller.

- Et hulsystem skal bestå af minimum én indgang.
- Et hulsystem kan bestå af flere indgange.
- Kaniner skal kunne grave nye indgange til hulsystemer, når de befinder sig i dem.

### Uge 2

#### K2-1a | Ulve kan placeres på kortet når input filerne beskriver dette.

- Ulve skal placeres tilfældigt på kortet når input filerne beskriver det.

#### K2-1b | Ulve kan dø, hvilket resulterer I at de fjernes fra verdenen.

- Ulve skal kunne dø og dermed blive fjernet fra verdenen.

#### K2-1c | Ulve jager andre dyr og spiser dem for at opnå energi.

- Ulve skal kunne jage andre dyr.
- Ulve skal kunne spise andre dyr.
- Ulve skal kunne få energi af at spise andre dyr.

#### K2-2a | Ulve er et flokdyr. De søger konstant mod andre ulve i flokken, og derigennem ’jager’ sammen. Når inputfilen beskriver (på en enkelt linje) at der skal placeres flere ulve, bør disse automatisk være i samme flok.

- Ulve skal være et flokdyr.
- Ulve skal søge mod andre ulve i flokken.
- Ulve skal jage sammen i flokken.
- Ulve skal placeres i samme flok når input filerne har mere end én ulv på samme linje.

#### K2-3a | Ulve og deres flok, tilhører en ulvehule, det er også her de formerer sig. Ulve ’bygger’ selv deres huler. Ulve kan ikke lide andre ulveflokke og deres huler. De prøver således at undgå andre grupper. Møder en ulv en ulv fra en anden flok, kæmper de mod hinanden.

- Ulve skal tilhøre en ulvehule.
- Ulve skal kunne bygge deres egen ulvehule.
- Ulve skal kunne lide andre ulve fra andre flokke.
- Ulve skal kunne undgå andre ulve fra andre flokke.
- Ulve skal kunne undgå ulvehuler fra andre flokke.
- Ulve skal kunne kæmpe mod andre ulve fra andre flokke hvis de mødes.

#### K2-4a | Kaniner frygter ulve og forsøger så vidt muligt at løbe fra dem.

- Kaniner skal frygte ulve.
- Kaniner skal løbe fra ulve.

#### K2-5a | Bjørne kan placeres på kortet når input filerne beskriver dette.

- Bjørne skal placeres tilfældigt på kortet når input filerne beskriver det.

#### K2-5b | Bjørne jager ligesom ulve, og spiser også alt.

- Bjørne skal kunne jage andre dyr.
- Bjørne skal kunne spise andre dyr.
- Bjørne skal kunne få energi af at spise andre dyr.

#### K2-5c | Kaniner frygter bjørne og forsøger så vidt muligt at løbe fra dem.

- Kaniner skal frygte bjørne.
- Kaniner skal løbe fra bjørne.

#### K2-6a | Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den ’bor’. Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra. Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet.

- Bjørne skal være territorielle.
- Bjørne skal knytte sig til et bestemt område.
- Bjørne skal sjældent bevæge sig ud af deres territorie.
- Bjørnes territorie centrum er deres startplacering på kortet.

#### K2-7a | Dertil spiser bjørne også bær fra buske (såsom blåbær og hindbær) når de gror i området. Bær er en god ekstra form for næring for bjørnen (om end det ikke giver samme mængde energi som når de spiser kød), men som det er med buske går der tid før bær gror tilbage. Bær skal indsættes på kortet når inputfilerne beskriver dette.

- Bjørne skal kunne spise bær.
- Bær giver ikke samme mængde energi som kød.
- Bær skal kunne vokse tilbage over tid.
- Bærbuske skal placeres tilfældigt på kortet når input filerne beskriver det.

#### K2-8a | Bjørnen er naturligvis vores øverste rovdyr i denne lille fødekæde, men det hænder at en stor nok gruppe ulve kan angribe (og dræbe) en bjørn. Dette vil i praksis være hvis flere ulve af samme flok er i nærheden af en bjørn

- 3 eller flere ulve skal kunne angribe og dræbe en bjørn.

#### KF2-1 (Frililigt) | Hvis den ene ulv bliver voldsomt såret, underkaster den sig den sejrende ulvs flok. En såret ulv har brug for hvile før den kan fortsætte.

- En såret ulv skal kunne underkaste sig den sejrende ulvs flok.
- En såret ulv skal hvile sig før den kan fortsætte.

#### KF2-2 (Frililigt) | Dog er bjørnen ikke et flokdyr, og mødes kun med andre bjørne når de skal parre sig. Bjørnen kan også dø og Iernes her fra verdenen.

- Bjørne skal kunne parre sig. Her skal bjørnen mødes med en anden bjørn.
- Bjørne skal kunne dø og fjernes fra verdenen.

#### KF2-3 (Frililigt) | Bjørne der ikke er klar Bl at parre sig, angriber andre bjørne der bevæger sig ind på deres områder. Tilsvarende angriber de andre dyr.

- Bjørne der ikke er klar til at parre sig skal kunne angribe andre bjørne der bevæger sig ind på deres områder.
- Bjørne angriber andre dyr der bevæger sig ind på deres områder.

### Uge 3

#### K3-1a | Opret ådsler, som placeres på kortet når input filerne beskriver dette.

- Ådsler skal placeres tilfældigt på kortet når input filerne beskriver det.

#### K3-1b | Når dyr dør nu, skal de efterlade et ådsel. Ådsler kan spises ligesom dyr kunne tidligere, dog afhænger mængden af ’kød’ af hvor stort dyret der døde er. Således spises dyr ikke direkte længere når det slås ihjel, i stedet spises ådslet. Alle dyr som er kødædende spiser ådsler.

- Når dyr dør skal de efterlade et ådsel.
- Ådsler skal kunne spises af kødædende dyr. Men kun hvis ådslet stammer fra et dyr de normalt ville spise.
- Mængden af kød i et ådsel afhænger af hvor stort dyret der døde er.

#### K3-1c | Ådsler bliver dårligere med tiden og nedbrydes helt – selvom det ikke er spist op (altså forsvinder det)! Det forsvinder naturligvis også hvis det hele er spist.

- Ådsler skal blive dårligere med tiden.
- Ådsler skal forsvinde hvis de nedbrydes helt.
- Ådsler skal forsvinde hvis de bliver spist op.

#### K3-2a | Udover at ådsler nedbrydes, så hjælper svampene til. Således kan der opstå svampe I et ådsel. Dette kan ikke ses på selve kortet, men svampen lever I selve ådslet. Når ådslet er nedbrudt (og forsvinder), og hvis svampen er stor nok, kan den ses som en svamp placeret på kortet, der hvor ådslet lå. For at læse inputfilerne skal du sikre dig, at et ådsel kan indlæses med svamp.

- Svampe skal kunne opstå i ådsler.
- Man kan ikke svampe i ådsler på kortet.
- Hvis et ådsel med svampe nedbrydes og forsvinder, skal svampen kunne ses på kortet.
- Der skal kunne indlæses ådsler med svampe fra inputfilerne.

#### K3-2b | Svampe kan kun overleve, hvis der er andre ådsler den kan sprede sig til i nærheden. Er dette ikke tilfældet, vil svampen også dø efter lidt tid. Desto større ådslet er, desto længere vil svampen leve efter ådslet er væk. Da svampen kan udsende sporer, kan den række lidt længere end kun de omkringliggende pladser.

- Svampe skal kunne sprede sig til andre ådsler i nærheden.
- Svampe skal dø hvis der ikke er andre ådsler i nærheden, som den kan sprede sig til.
- Svampe skal kunne leve længere hvis ådslet det stammer fra er større.
- Svampe skal kunne sprede sig til pladser i et større område, ved at udsende sporer.

#### KF3-1a (Friviligt) | Når en svamp dør, er jorden ekstra gunstig. Derfor opstår græs på sådanne felter, når svampen dør.

- Når en svamp dør skal der opstå græs på det felt den døde på.

#### KF3-2a (Friviligt) | Ikke alle typer svampe lever på døde dyr. Der eksisterer også en anden type svamp (Cordyceps). Denne svamp spreder sig til, og styrer, (kun) levende dyr. Deres livscyklus er den samme som de tidligere svampe; de nedbryder langsomt dyret, og er der ikke mere tilbage af dyret, dør svampen snart efter. Når svampen har tæret nok på dyret, dør dyret. Da denne svamp nedbryder dyret mens det lever, er der ikke noget ådsel efter døden. Svampen dør også når dyret dør, med undtagelsen af krav.

- Der skal kunne eksistere en anden type svamp (Cordyceps).
- Cordyceps skal kunne sprede sig til levende dyr.
- Cordyceps efterlader ikke et ådsel når dyret dør.
- Cordyceps skal kunne nedbryde dyret mens det lever.
- Cordyceps forsøger at finde et andet dyr at sprede sig til, når det dyr det lever på dør. Hvis det ikke lykkes, dør svampen.

#### KF3-3a (Friviligt) | Når Cordyceps’ vært dør, forsøger den at sprede sig Bl levende dyr i nærheden, og kun på dette tidspunkt. Igen kan denne svamp sprede sig lidt længere end de omkringliggende pladser da den sender sporer ud.

- Cordyceps skal forsøge at sprede sig til levende dyr i nærheden, når dens vært dør.
- Cordyceps skal dø hvis den ikke kan sprede sig til et andet dyr, når dens vært dør.
- Cordyceps skal kunne sprede sig til pladser i et større område, ved at udsende sporer.

#### KF3-3b (Friviligt) | Når et dyr er inficeret med Cordyceps svampen, overtager svampen dyrets handlinger. Dyret gør derfor som svampen bestemmer, hvilket er at søge mod andre dyr af samme art.

- Når et dyr er inficeret med Cordyceps svampen, skal svampen overtage dyrets handlinger.
- Dyret skal bevæge sig mod andre dyr af samme art, når det er inficeret med Cordyceps svampen.
