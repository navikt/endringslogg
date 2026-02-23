# Endringslogg
Endringsloggen kan brukes til å publisere meldinger om endringer som skjer i løsningen din.

## Publisering

Vi bruker GitHub Release-notater som versjonslogg.

### Release-flyt

1. Oppdater versjon i `frontend/package.json`.
2. Merge til `main`.
3. Gå til GitHub → **Releases** → **Draft a new release**.
4. Lag ny tag `v<versjon>` i release-dialogen.
5. Skriv release-notater og publiser releasen.
6. `Publish`-workflowen kjører ved `release.published` og publiserer pakken.
