import { BusinessErrorCode } from "../exceptions/business-errors";
import { AppErrorFactory } from "../exceptions/error-factory";

export type User = {
    id: number;
    username: string;
    email: string;
    password: string;
    token?: string;
};

export type ValidationRules = Partial<Record<keyof User, boolean>>;

/**
 * Validates user properties based on the specified validation rules.
 *
 * @param {Partial<User>} user - The user object containing the properties to validate.
 * @param {ValidationRules} rules - The validation rules indicating which properties to check.
 *
 * @throws Will throw an error if any of the validated properties do not meet the required criteria.
 *
 * @example
 * // Validate only email (e.g., registration scenario)
 * validate({ email: "invalidemail" }, { email: true });
 * // Throws an error: invalid email format
 *
 * @example
 * // Validate all fields (e.g., user creation)
 * validate(
 *   { username: "user", email: "email@domain.com", password: "secure123" },
 *   { username: true, email: true, password: true }
 * );
 * // No error, all fields are valid
 */
export const validate = (user: Partial<User>, rules: ValidationRules) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (rules.email && user.email && !emailRegex.test(user.email)) {
        throw AppErrorFactory.businessError(BusinessErrorCode.EMAIL_FORMAT_NOT_VALID);
    }

    if (rules.username && user.username && user.username.length < 3) {
        throw AppErrorFactory.businessError(BusinessErrorCode.USERNAME_EMPTY);
    }

    if (rules.password && user.password && user.password.length < 5) {
        throw AppErrorFactory.businessError(BusinessErrorCode.PASSWORD_FORMAT_NOT_VALID);
    }
};
