import { BusinessErrorCode } from "~/domain/exceptions/business-errors";
import { AppErrorFactory } from "~/domain/exceptions/error-factory";
import { User } from "~/domain/models/user";
import { UserRepositoryI } from "~/domain/repositories/user-repository";
import { GetUserUseCaseI } from "~/domain/usecases/get-user-usecase";

export const getUserUseCase: GetUserUseCaseI = {
    /**
     * @param {number} id
     * @param {UserRepositoryI} repo
     * @returns {Promise<User>} Resolves with the user object.
     * @throws {Error} If the user is not found or a request error occurs.
     */
    execute: async (id: number, repo: UserRepositoryI): Promise<User> => {
        const user = await repo.getUserById(id);
        if (!user) {
            throw AppErrorFactory.businessError(BusinessErrorCode.USER_NOT_FOUND);
        }
        return user;
    },
};
