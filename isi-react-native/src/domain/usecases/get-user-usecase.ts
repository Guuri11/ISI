import { User } from "~/domain/models/user";
import { UserRepositoryI } from "~/domain/repositories/user-repository";

export interface GetUserUseCaseI {
    execute: (id: number, repo: UserRepositoryI) => Promise<User>;
}
