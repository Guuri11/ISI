import { BusinessErrorCode } from "~/domain/exceptions/business-errors";
import { AppErrorFactory } from "~/domain/exceptions/error-factory";
import { validate } from "~/domain/models/user";
import { UserRepositoryI } from "~/domain/repositories/user-repository";
import { LoginUserUseCaseI } from "~/domain/usecases/login-user-usecase";

export const loginUserUseCase: LoginUserUseCaseI = {
    /**
     * @param {string} username
     * @param {string} password
     * @param {UserRepositoryI} repo
     * @returns {Promise<string>} Resolves with the authentication token.
     * @throws {Error} If validation or authentication fails.
     */
    execute: async (username: string, password: string, repo: UserRepositoryI): Promise<string> => {
        validate({ username, password }, { username: true, password: true });

        const token = await repo.login(username, password);
        if (!token) {
            throw AppErrorFactory.businessError(BusinessErrorCode.INVALID_CREDENTIALS);
        }
        return token;
    },
};
