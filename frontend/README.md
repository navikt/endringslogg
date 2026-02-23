# Endringslogg

Frontend-komponent for endringslogg

## Versjonslogg

Vi bruker GitHub Release-notater som eneste kilde for versjoneringshistorikk.


## Publisering

Publisering skjer manuelt via GitHub Release. Tag opprettes manuelt i "New release" på GitHub.

### Flyt

1. Oppdater `frontend/package.json` med ny versjon.
2. Merge til `main`.
3. Gå til GitHub → **Releases** → **Draft a new release**.
4. Lag ny tag `v<versjon>` i release-dialogen.
5. Skriv/generer gode release-notater og publiser releasen.
6. Workflowen `Publish` kjører ved `release.published` og publiserer pakken.

### Viktig

- Selve publiseringen skjer ikke automatisk ved merge; den skjer først når release publiseres manuelt.

## Installasjon

```sh
npm install @navikt/endringslogg
# eller hvis du bruker yarn:
yarn add @navikt/endringslogg
```
