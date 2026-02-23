# Endringslogg

## Publisering

Vi bruker GitHub Release-notater som versjonslogg. 

### Release-flyt

1. Oppdater versjon i `frontend/package.json`.
2. Merge til `main`.
3. `Auto Tag` lager `v<versjon>` automatisk når versjonen er endret.
4. Opprett/publiser GitHub Release fra taggen, med release-notater.
5. `Publish`-workflowen kjører ved `release.published` og publiserer pakken.
