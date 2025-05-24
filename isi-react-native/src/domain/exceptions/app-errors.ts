import { BusinessErrorCode } from "./business-errors";
import { ErrorType } from "./error-types";

export interface AppError {
    type: ErrorType;
    code?: BusinessErrorCode | string;
    cause?: unknown;
}
