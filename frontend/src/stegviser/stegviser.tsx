import * as React from 'react';
import './stegviser.css';

interface StegviserProps {
    antallSteg: number;
    valgtSteg: number;
}

const Stegviser = (props: StegviserProps) => {
    const mapTilSteg = (antall: number, selectedIdx: number) => {
        return new Array(antall).fill(0).map((_, i) => (
            <div
                key={i}
                className={`stegviser__steg${i === selectedIdx ? ' stegviser__steg--selected' : ''}`}
            />
        ));
    };

    return (
        <div className={'stegviser'} data-testid="endringslogg_stegviser">
            {mapTilSteg(props.antallSteg, props.valgtSteg)}
        </div>
    );
};

export default Stegviser;
