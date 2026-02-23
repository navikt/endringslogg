import type { Meta, StoryObj } from '@storybook/react';
import Endringslogg from './src';
import { EndringsloggEntryWithSeenStatus } from './src/utils/endringslogg-custom';

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

const meta: Meta<typeof Endringslogg> = {
    title: 'Komponenter/Endringslogg',
    component: Endringslogg,
    args: {
        userId: 'storybook-user',
        appId: 'ENDRINGSLOGG',
        appName: 'Endringslogg demo',
        backendUrl: 'https://example.invalid',
        localData: shortMockData,
    },
};

export default meta;

type Story = StoryObj<typeof Endringslogg>;

export const Standard: Story = {};

export const MangeMockData: Story = {
    args: {
        localData: largeMockData,
    },
};

export const MedModalOgLenker: Story = {
    args: {
        localData: withModalMockData,
    },
};

export const LysVariant: Story = {
    args: {
        stil: 'lys',
        localData: shortMockData,
    },
    decorators: [
        StoryComponent => (
            <div style={{ backgroundColor: '#1f2937', padding: '2rem' }}>
                <StoryComponent />
            </div>
        ),
    ],
};
