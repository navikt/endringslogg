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
Deployes automatisk ved push til `main` (endringer i `sanity/`).

## Backend

Henter endringslogg-innhold fra Sanity og fungerer som proxy for frontend-applikasjonene.
Lagrer hvilke endringer saksbehandlere har lest i en database.

### Tilgang

For at en frontend-applikasjon skal få tilgang må URL-en den kjører på legges inn i CORS-filteret i `backend/src/main/kotlin/no/nav/familie/Application.kt`.

## Ny saksbehandlingsløsning?

1. Legg til ny løsning med app-id i `sanity/schemas/schema.ts`
2. Legg inn nye URL-er i CORS-filteret i `backend/src/main/kotlin/no/nav/familie/Application.kt`
3. Legg til nye personer i Sanity Studio som får tilgang til å redigere innhold

## Henvendelser

Interne henvendelser kan sendes via Slack i kanalen #team-sykdom-i-familien.
