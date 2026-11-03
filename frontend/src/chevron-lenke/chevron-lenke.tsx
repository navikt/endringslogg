import * as React from 'react';
import './chevron-lenke.css';
import { ArrowLeftIcon, ArrowRightIcon } from '@navikt/aksel-icons';

export enum Direction {
    RIGHT,
    LEFT,
}

interface ChevronLenkeProps {
    retning: Direction;
    tekst: string;
    hide?: boolean;
    dataTestId: string;

    onClick(event: React.MouseEvent<HTMLButtonElement>): void;
}

const ChevronLenke = (props: ChevronLenkeProps) => {
    const { retning, tekst, onClick, hide, dataTestId } = props;
    return (
        <button className={`chevron-lenke${hide ? ' chevron-lenke--hide' : ''}`} onClick={onClick} data-testid={dataTestId}>
            {retning === Direction.RIGHT ? (
                <>
                    <span className={'chevron-lenke__tekst'} role="button">
                        {tekst}
                    </span>
                    <ArrowRightIcon
                        className={'chevron-right'}
                    />
                </>
            ) : (
                <>
                    <ArrowLeftIcon
                        className={'chevron-left'}
                    />
                    <span className={'chevron-lenke__tekst'} role="button">
                        {tekst}
                    </span>
                </>
            )}
        </button>
    );
};

export default ChevronLenke;
