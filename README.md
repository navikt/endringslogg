# Endringslogg

Monorepo for endringslogg-løsningen. Endringsloggen publiserer meldinger om endringer i saksbehandlingsløsninger.

## Struktur

| Mappe | Beskrivelse |
|-------|------------|
| `frontend/` | React-komponent (`@navikt/endringslogg`) som konsumeres av saksbehandlingsløsningene |
| `sanity/` | Sanity Studio for redaksjonelt innhold |
| `backend/` | Ktor-backend som henter innhold fra Sanity og holder styr på leste endringer |

## Frontend-komponent

Se [frontend/README.md](frontend/README.md) for installasjon, publisering og versjonslogg.

## Sanity Studio

Innhold redigeres på [k9-endringslogg.sanity.studio](https://k9-endringslogg.sanity.studio/production/desk).
1. Personen må be om tilgang til sanity i https://myapplications.microsoft.com/
2. Logg inn med SSO og organization slug `navikt`.
3. Admin må inn og gi skrivetilgang for de som skal kunne skrive innlegg

## Backend

Henter endringslogg-innhold fra Sanity og fungerer som proxy for frontend-applikasjonene.
Lagrer hvilke endringer saksbehandlere har lest i en database.

### Tilgang

For at en frontend-applikasjon skal få tilgang må URL-en den kjører på legges inn i CORS-filteret i `backend/src/main/kotlin/no/nav/familie/Application.kt`.

## Ny saksbehandlingsløsning?

1. Legg til ny løsning med app-id i `sanity/schemas/schema.ts`
2. Legg inn nye URL-er i CORS-filteret i `backend/src/main/kotlin/no/nav/familie/Application.kt`
3. Legg til nye personer i Sanity Studio som får tilgang til å redigere innhold

## Migrere fra familie-endringslogg?

1. Erstatt `@navikt/familie-endringslogg` med `@navikt/endringslogg`
2. Bytt url til `https://endringslogg.intern.nav.no`
3. Informer de som legger inn innlegg i Sanity om å bruke [k9-endringslogg.sanity.studio](https://k9-endringslogg.sanity.studio/production/desk).
4. Ettersom vi har fersk database er det ikke lagret hvilke innlegg saksbehandler har sett før migrering.
   - Dersom dere har brukt tvungen modal på tidligere innlegg kan det føre til at disse popper opp for saksbehandler første gangen de er innom applikasjonen, derfor anbefales det å toggle av tvungen         visning på gamle innlegg.
   - De vil også få notifikasjon på endringslogg-ikonet for alle innleggene som finnes, men det forsvinner hvis man åpner og lukker innholdet i endringsloggen. 

## Henvendelser

Interne henvendelser kan sendes via Slack i kanalen #team-sykdom-i-familien.
