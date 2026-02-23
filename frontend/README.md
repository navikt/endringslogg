# Endringslogg

Komponent for endringslogg for team familie.

> 💡 Ser du etter [dokumentasjon](https://navikt.github.io/familie-felles-frontend/?path=/docs/komponenter-endringslogg--standard)?

## Versjonslogg

Vi bruker GitHub Release-notater som eneste kilde for versjoneringshistorikk.

## Publisering

Publisering skjer manuelt via GitHub Release, mens tag opprettes automatisk når versjon endres.

### Flyt

1. Oppdater `frontend/package.json` med ny versjon.
2. Merge til `master`.
3. Workflowen `Auto Tag` oppretter automatisk tag `v<versjon>` når versjonen faktisk har endret seg.
4. Opprett/publiser en GitHub Release fra taggen med gode release-notater.
5. Workflowen `Publish` kjører ved `release.published` og publiserer pakken.

### Viktig

- Dependabot-PRer uten versjonsbump lager ikke nye tagger.
- Selve publiseringen skjer ikke automatisk ved merge; den skjer først når release publiseres manuelt.

## Installasjon

```sh
npm install @navikt/familie-endringslogg
# eller hvis du bruker yarn:
yarn add @navikt/familie-endringslogg
```
