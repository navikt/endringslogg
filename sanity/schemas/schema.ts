import blockContent from './blockContent'
import {endringsloggSchema} from './endring'

export const schemaTypes = [
  endringsloggSchema("K9_SAK", "Sykdom i familien"),
  endringsloggSchema("AAP", "Arbeidsavklaringspenger"),

  blockContent,
];
