import blockContent from './blockContent'
import {endringsloggSchema} from './endring'

export const schemaTypes = [
  endringsloggSchema("EF", "Enslig forsørger"),
  endringsloggSchema("BAKS", "Barnetrygd"),
  endringsloggSchema("K9_SAK", "Sykdom i familien"),
  endringsloggSchema("TS", "Tilleggsstønader"),
  endringsloggSchema("AAP", "Arbeidsavklaringspenger"),

  blockContent,
];
