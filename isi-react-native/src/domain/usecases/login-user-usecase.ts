import { UserRepositoryI } from "~/domain/repositories/user-repository";

export interface LoginUserUseCaseI {
    execute: (username: string, password: string, repo: UserRepositoryI) => Promise<string>;
}
