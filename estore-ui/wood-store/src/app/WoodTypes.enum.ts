/**
 * Wood type options
 * String matches backend
 */
export enum WoodTypesEnum {
    ASH = "ASH",
    BIRCH = "BIRCH",
    CEDAR = "CEDAR",
    CHERRY = "CHERRY",
    FIR = "FIR",
    MAHOGONY = "MAHOGONY",
    MAPLE = "MAPLE",
    OAK = "OAK",
    PINE = "PINE",
    POPLAR = "POPLAR",
    REDWOOD = "REDWOOD",
    TEAK = "TEAK",
    WALNUT = "WALNUT"
}

/**
 * Maps the wood type to the way the string is displayed
 */
export const WoodType2LabelMapping: Record<WoodTypesEnum,string> = {
    [WoodTypesEnum.ASH]: "Ash",
    [WoodTypesEnum.BIRCH]: "Birch",
    [WoodTypesEnum.CEDAR]: "Cedar",
    [WoodTypesEnum.CHERRY]: "Cherry",
    [WoodTypesEnum.FIR]: "Fir",
    [WoodTypesEnum.MAHOGONY]: "Mahogany",
    [WoodTypesEnum.MAPLE]: "Maple",
    [WoodTypesEnum.OAK]: "Oak",
    [WoodTypesEnum.PINE]: "Pine",
    [WoodTypesEnum.POPLAR]: "Poplar",
    [WoodTypesEnum.REDWOOD]: "Redwood",
    [WoodTypesEnum.TEAK]: "Teak",
    [WoodTypesEnum.WALNUT]: "Walnut"
};