/**
 * Varnish type options
 * String matches backend
 */
export enum VarnishTypesEnum {
    GLOSS = "GLOSS",
    SEMIGLOSS = "SEMIGLOSS",
    SATIN = "SATIN",
    MATTE = "MATTE"
}

/**
 * Maps the varnish type to the way the string is displayed
 */
export const VarnishType2LabelMapping: Record<VarnishTypesEnum, string> = {
    [VarnishTypesEnum.GLOSS]: "Gloss",
    [VarnishTypesEnum.SEMIGLOSS]: "Semi-Gloss",
    [VarnishTypesEnum.SATIN]: "Satin",
    [VarnishTypesEnum.MATTE]: "Matte"
};