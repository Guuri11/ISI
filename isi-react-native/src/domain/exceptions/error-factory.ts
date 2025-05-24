import { AppError } from "./app-errors";
import { BusinessErrorCode } from "./business-errors";
import { ErrorType } from "./error-types";

export const AppErrorFactory = {
    authError(code?: string): AppError {
        console.warn("Auth error:", code);
        return { type: ErrorType.AUTHENTICATION, code };
    },
    authorizationError(): AppError {
        console.warn("Authorization error");
        return { type: ErrorType.AUTHORIZATION };
    },
    networkError(): AppError {
        console.warn("Network error");
        return { type: ErrorType.NETWORK };
    },
    businessError(code: BusinessErrorCode): AppError {
        console.warn(code);
        return { type: ErrorType.BUSINESS, code };
    },
    unknownError(cause?: unknown): AppError {
        console.error("Unknown error");
        return { type: ErrorType.UNKNOWN, cause };
    },
};
