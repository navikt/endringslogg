import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import '@navikt/ds-css';
import Endringslogg from '../src';
import { EndringsloggEntryWithSeenStatus } from '../src/utils/endringslogg-custom';

const toPortableText = (text: string) => [
    {
        _type: 'block',
        style: 'normal',
        markDefs: [],
        children: [
            {
                _type: 'span',
                marks: [],
                text,
            },
        ],
    },
];

const buildMockEntries = (count: number): EndringsloggEntryWithSeenStatus[] => {
    return Array.from({ length: count }, (_, index) => {
        const entryNumber = count - index;
        const month = ((index % 12) + 1).toString().padStart(2, '0');
        const day = ((index % 28) + 1).toString().padStart(2, '0');

        return {
            _id: `mock-entry-${entryNumber}`,
            title: `Forbedring ${entryNumber}: Oppdatert brukerflyt`,
            date: `2026-${month}-${day}T10:00:00.000Z`,
            description: toPortableText(
                `Dette er en mock-beskrivelse for endring ${entryNumber}. Vi har forbedret navigasjon, tilgjengelighet og tekstinnhold.`,
            ),
            seen: index > 4,
            seenForced: true,
            forced: false,
            linkText: index % 3 === 0 ? 'Les mer om endringen' : undefined,
            link: index % 3 === 0 ? `https://example.com/endring/${entryNumber}` : undefined,
        };
    });
};

const shortMockData = buildMockEntries(8);
const largeMockData = buildMockEntries(36);

const withModalMockData: EndringsloggEntryWithSeenStatus[] = [
    {
        _id: 'mock-modal-1',
        title: 'Ny veiviser tilgjengelig',
        date: '2026-02-20T10:00:00.000Z',
        description: toPortableText('Vi har laget en stegvis introduksjon til ny funksjonalitet.'),
        seen: false,
        seenForced: true,
        forced: false,
        modal: {
            header: 'Ny funksjon',
            slides: [
                {
                    slideHeader: 'Steg 1',
                    slideDescription: toPortableText('Start i oversikten for å se nye valg.'),
                },
                {
                    slideHeader: 'Steg 2',
                    slideDescription: toPortableText('Bruk filtrene for raskere navigasjon.'),
                },
            ],
        },
        linkText: 'Åpne dokumentasjon',
        link: 'https://example.com/dokumentasjon',
    },
    ...buildMockEntries(10),
];

const baseProps = {
    userId: 'playground-user',
    appId: 'ENDRINGSLOGG',
    appName: 'Endringslogg demo',
    backendUrl: 'https://example.invalid',
};

const Variant = ({ title, children }: { title: string; children: React.ReactNode }) => (
    <section style={{ marginBottom: '3rem' }}>
        <h2>{title}</h2>
        <div style={{ position: 'relative' }}>{children}</div>
    </section>
);

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <div style={{ padding: '2rem' }}>
            <Variant title="Standard">
                <Endringslogg {...baseProps} localData={shortMockData} />
            </Variant>
            <Variant title="Mange mock-data">
                <Endringslogg {...baseProps} localData={largeMockData} />
            </Variant>
            <Variant title="Med modal og lenker">
                <Endringslogg {...baseProps} localData={withModalMockData} />
            </Variant>
            <Variant title="Lys variant">
                <div style={{ backgroundColor: '#1f2937', padding: '2rem' }}>
                    <Endringslogg {...baseProps} stil="lys" localData={shortMockData} />
                </div>
            </Variant>
        </div>
    </StrictMode>,
);
